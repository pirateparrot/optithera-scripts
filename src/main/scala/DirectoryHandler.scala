import com.opencsv._
import java.io._
import scala.util.Try
import scala.collection.JavaConversions._
import org.bdgenomics.formats.avro._

class DirectoryHandler(prefix: String, genotypeVersion: String,
  batchData: collection.mutable.HashMap[String,Batch], dateFormat: java.text.SimpleDateFormat) {

  val NB_INDIVIDUAL_COLUMNS = 11
  val filePattern = """^Individuals_([0-9]+)(_(.*))?\.tsv$""".r
  val genotypePattern = """^Individuals_([0-9]+)_(v[0-9]+)_Genotypes\.tsv$""".r

  def ignore(possibleFailure: () => Unit) =
    try {
      possibleFailure()
    } catch {
      case _ : Throwable =>
    }

  def processIndividual(id: String) : Option[Individual] = {
    val ls = new java.io.File(prefix + id).listFiles.map(file => file.getName).sorted
    val record = IndividualRecord()

    ls.foreach({ fileName =>
      filePattern.findAllIn(fileName).matchData foreach { m =>

        val reader = new CSVIterator(new CSVReader(new FileReader(prefix + id + "/" + fileName)))
        reader.next() // Skip headers

        m.group(3) match {
          case ("Visits") =>
            reader.foreach { row =>
              val visit = Visit.newBuilder()
              visit.setVisitId(row(0))
              ignore(() => visit.setVisitDateEpoch(dateFormat.parse(row(1)).getTime))
              visit.setStudyId(row(2))
              visit.setIsFasting(Try(java.lang.Boolean.valueOf(row(3))).getOrElse(false))
              visit.setDescription(row(4))
              record.visits = visit :: record.visits
            }
          case ("Phenotypes") =>
            reader.foreach { row =>
              val phenotype = Phenotype.newBuilder()
              phenotype.setPhenotypeType(row(1))
              phenotype.setPhenotypeGroup(row(2))
              phenotype.setName(row(3))
              ignore(() => phenotype.setMeasureDataType(MeasureDataType.valueOf(row(4))))
              phenotype.setMeasure(row(5))
              phenotype.setMeasureUnits(row(6))
              phenotype.setDescription(row(7))
              ignore(() => phenotype.setDiagnosisDateEpoch(dateFormat.parse(row(8)).getTime))
              record.phenotypes = phenotype :: record.phenotypes
            }
          case (null) =>
            reader.foreach { row =>

              val gender = if (Character.toLowerCase(row(5).charAt(0)) == 'm') Gender.Male
                else if (Character.toLowerCase(row(5).charAt(0)) == 'f') Gender.Female
                else Gender.Unknown
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
                if (batchId == "") None else batchData.get(batchId)
              }).flatten
              individual.setBatches(listBatches)
              record.individual = Some(individual)
            }
          case (_) =>
            genotypePattern.findAllIn(fileName).matchData foreach { n =>
              if (n.group(2) == genotypeVersion) reader.foreach { row =>
                val contig = Contig.newBuilder()
                contig.setContigName(row(5)) // chromosome
                contig.setAssembly("NCBI36") // Verify this
                contig.setSpecies("human")

                // DBSNP data
                val variant = Variant.newBuilder()
                variant.setContig(contig.build())
                ignore(() => variant.setStart(java.lang.Long.parseLong(row(6))))
                ignore(() => variant.setEnd(java.lang.Long.parseLong(row(7))))
                variant.setReferenceAllele(row(8))
                variant.setAlternateAllele(row(9))

                val alleles: List[GenotypeAllele] = List() // List of GenotypeAllele Ref, Alt, OtherAlt, NoCall

                val genotype = Genotype.newBuilder()
                genotype.setVariant(variant.build())
                genotype.setSampleId(row(2))
                genotype.setAlleles(alleles)
                record.genotypes = genotype :: record.genotypes
              }
            }
        }
      }
    })

    if (record.isReady) {
      Some(record.toWritable())
    } else {
      None
    }

  }
}
