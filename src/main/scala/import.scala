import au.com.bytecode.opencsv._
import java.io._
import scala.util.Try
import scala.collection.JavaConversions._
import org.bdgenomics.formats.avro._

class IndividualRecord {
  var individual: Option[Individual.Builder] = _
  var visits: List[Visit.Builder] = _
  var phenotypes: List[Phenotype.Builder] = _
  var genotypes: List[Genotype.Builder] = _
  def isReady(nbGenotypes : Int) = {
    !(this.individual.isEmpty || this.visits.length == 0 ||
    this.phenotypes.length == 0 || this.genotypes.size < nbGenotypes)
  }
  def toWritable() = {
    val listPhenotypes = this.phenotypes.map { p => p.build() }
    val listGenotypes = this.genotypes.map { g => g.build() }
    val listVisits = this.visits.map { visit =>
      visit.setPhenotypes(listPhenotypes)
      visit.build()
    }
    this.individual.get.setVisits(listVisits)
    this.individual.get.setGenotypes(listGenotypes)
    this.individual.get.build
  }
}

object IndividualRecord {
  def apply (): IndividualRecord = {
    var ind = new IndividualRecord
    ind.individual = None
    ind.visits = List()
    ind.phenotypes = List()
    ind.genotypes = List()
    ind
  }
}

object Importer {
  def ignore(possibleFailure: () => Unit) =
    try {
      possibleFailure()
    } catch {
      case _ : Throwable =>
    }

    // It blows my mind that this isn't part of the standard library
    def flattenOptionList[T](list: List[Option[T]]) : List[T] = {
      list.filter(x => !x.isEmpty).map(x => x.get)
    }

  def main(args: Array[String]) = {

    val individualsById = new collection.mutable.HashMap[String,IndividualRecord]
    val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")
    val prefix = "./output/"
    val NB_INDIVIDUAL_COLUMNS = 11
    val filePattern = """^Individuals_([0-9]+)(_(.*))?\.tsv$""".r
    val genotypePattern = """^Individuals_([0-9]+)_(v[0-9]+)_Genotypes\.tsv$""".r
    var nbGenotypeBatches : Option[Int] = None
    val batchData = new collection.mutable.HashMap[String,Batch]()
    var writeToParquet = scala.collection.mutable.Buffer[Individual]()
    //val parquetWriter = new ParquetWriter[Individual](writeToParquet, "./parquet/individuals", Individual.getClassSchema)

    // Load the batches first
    val reader = new CSVReader(new FileReader(prefix + "Batches.tsv"))
    reader.readNext() // Skip headers
    reader.readAll.foreach { row =>
      val batch = Batch.newBuilder()
      batch.setBatchDateEpoch(dateFormat.parse(row(1)).getTime)
      batch.setBatchType(row(2))
      batch.setDescription(row(3))
      batchData += row(0) -> batch.build()
    }

    // Then everything else
    val list = new java.io.File(prefix).listFiles.map(file => file.getName).sorted
    list.foreach({ fileName =>
      filePattern.findAllIn(fileName).matchData foreach { m =>

        val current = individualsById.get(m.group(1)).getOrElse(IndividualRecord())
        val reader = new CSVReader(new FileReader(prefix + fileName))
        reader.readNext() // Skip headers

        (m.group(1), m.group(3)) match {
          case (id, "Visits") =>
            reader.readAll.foreach { row =>
              val visit = Visit.newBuilder()
              visit.setVisitId(row(0))
              ignore(() => visit.setVisitDateEpoch(dateFormat.parse(row(1)).getTime))
              visit.setStudyId(row(2))
              visit.setIsFasting(Try(java.lang.Boolean.valueOf(row(3))).getOrElse(false))
              visit.setDescription(row(4))
              current.visits = visit :: current.visits
            }
          case (id, "Phenotypes") =>
            reader.readAll.foreach { row =>
              val phenotype = Phenotype.newBuilder()
              phenotype.setPhenotypeType(row(1))
              phenotype.setPhenotypeGroup(row(2))
              phenotype.setName(row(3))
              ignore(() => phenotype.setMeasureDataType(MeasureDataType.valueOf(row(4))))
              phenotype.setMeasure(row(5))
              phenotype.setMeasureUnits(row(6))
              phenotype.setDescription(row(7))
              ignore(() => phenotype.setDiagnosisDateEpoch(dateFormat.parse(row(8)).getTime))
              current.phenotypes = phenotype :: current.phenotypes
            }
          case (id, null) =>
            reader.readAll.foreach { row =>

              // Set the nb of genotypes the first time
              if (nbGenotypeBatches.isEmpty)
                nbGenotypeBatches = Some(row.length - NB_INDIVIDUAL_COLUMNS)

              val gender = if (row(5).charAt(0) == 'm') Gender.Male else if (row(5).charAt(0) == 'f') Gender.Female else Gender.Unknown
              val individual = Individual.newBuilder()
              individual.setGender(gender)
              individual.setIndividualId(row(0))
              individual.setFamilyId(row(1))
              individual.setFatherId(row(2))
              individual.setMotherId(row(3))
              ignore(() => individual.setBirthDateEpoch(dateFormat.parse(row(4)).getTime))
              individual.setEthnicCode(row(6))
              individual.setCentreName(row(7))
              individual.setRegion(row(8))
              individual.setCountry(row(9))
              individual.setNotes(row(10))
              val listBatches = row.toList.drop(NB_INDIVIDUAL_COLUMNS).map({batchId =>
                if (batchId == "None") None else batchData.get(batchId)
              })
              individual.setBatches(flattenOptionList(listBatches))
              current.individual = Some(individual)
            }
          case (id, _) =>
            genotypePattern.findAllIn(fileName).matchData foreach { n =>
              // This might be useful in the future:
              // val version = n.group(2)

              // println(row)

              val contig = Contig.newBuilder()
              // contig.setContigName() // chromosome
              contig.setAssembly("NCBI36") // Verify this
              contig.setSpecies("human")

              val variant = Variant.newBuilder()
              variant.setContig(contig.build())
              // variant.setStart() // position
              // variant.setEnd() // long
              // variant.setReferenceAllele() // str
              // variant.setAlternateAllele() // str

              val alleles: List[GenotypeAllele] = List() // List of GenotypeAllele Ref, Alt, OtherAlt, NoCall

              val genotype = Genotype.newBuilder()
              genotype.setVariant(variant.build())
              // genotype.setSampleId() // str
              genotype.setAlleles(alleles)
              current.genotypes = genotype :: current.genotypes
            }
          case _ => throw new Exception("Invalid file name: " + fileName)
        }

        if (current.isReady(nbGenotypeBatches.getOrElse(999))) {
          println("Ready!")
          writeToParquet.append(current.toWritable())
        }

        individualsById += m.group(1) -> current
      }
    })

    // Persist
    //parquetWriter.persistData

  }
}
