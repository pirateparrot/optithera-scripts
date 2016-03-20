import au.com.bytecode.opencsv._
import java.io._
import scala.util.Try
import scala.collection.JavaConversions._
import org.bdgenomics.formats.avro._

class IndividualRecord {
  var individual: Option[collection.mutable.Buffer[Individual]] = _
  var visits:  Option[collection.mutable.Buffer[Visit]] = _
  var phenotypes:  Option[collection.mutable.Buffer[Phenotype]] = _
}

object IndividualRecord {
  def apply (): IndividualRecord = {
    var ind = new IndividualRecord
    ind.individual = None
    ind.visits = None
    ind.phenotypes = None
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

  def main(args: Array[String]) =
    println("Hello World!!")

    val individualsById = new collection.mutable.HashMap[String,IndividualRecord]
    val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")
    val prefix = "./output/"
    val filePattern = """^Individuals_([0-9]+)(_(.*))?\.tsv$""".r
    val genotypePattern = """^Individuals_([0-9]+)_(v[0-9]+)_Genotypes\.tsv$""".r

    val list = new java.io.File(prefix).listFiles.map(file => file.getName).sorted
    list.foreach(fileName =>
      filePattern.findAllIn(fileName).matchData foreach {m =>

        val current = individualsById.get(m.group(1)).getOrElse(IndividualRecord())
        val reader = new CSVReader(new FileReader(prefix + fileName))
        reader.readNext() // Skip headers

        (m.group(1), m.group(3)) match {
          case (id, "Visits") =>
            current.visits = Some(reader.readAll.map { row =>
              val visit = Visit.newBuilder()
              visit.setVisitId(row(0))
              ignore(() => visit.setVisitDateEpoch(dateFormat.parse(row(1)).getTime))
              visit.setStudyId(row(2))
              visit.setIsFasting(Try(java.lang.Boolean.valueOf(row(3))).getOrElse(false))
              visit.setDescription(row(4))
              // visit.setPhenotypes(phenotypes) // TODO
              visit.build()
            })
          case (id, "Phenotypes") =>
            current.phenotypes = Some(reader.readAll.map { row =>
              val phenotype = Phenotype.newBuilder()
              phenotype.setPhenotypeType(row(1))
              phenotype.setPhenotypeGroup(row(2))
              phenotype.setName(row(3))
              ignore(() => phenotype.setMeasureDataType(MeasureDataType.valueOf(row(4))))
              phenotype.setMeasure(row(5))
              phenotype.setMeasureUnits(row(6))
              phenotype.setDescription(row(7))
              ignore(() => phenotype.setDiagnosisDateEpoch(dateFormat.parse(row(8)).getTime))
              phenotype.build()
            })
          case (id, null) =>
            current.individual = Some(reader.readAll.map { row =>
              val gender = if (row(5).charAt(0) == 'm') Gender.Male else if (row(5).charAt(0) == 'f') Gender.Female else Gender.Unknown
              val individual = Individual.newBuilder()
              // individual.setGenotypes(genotypes) // TODO
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
              // individual.setVisits(visits) // TODO
              // individual.setBatches(batches) // TODO
              individual.build()
            })
          case (id, _) =>
            genotypePattern.findAllIn(fileName).matchData foreach {n =>
              // println(n.group(1)) // id
              // println(n.group(2)) // version
            }
          case _ => throw new Exception("Invalid file name: " + fileName)
        }

        // println(current.visits.getOrElse("EMPTY"))
        // current.visits = Some(m.group(3))
        individualsById += m.group(1) -> current

      }
    )


  //     val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")

  //     val reader = new CSVReader(new FileReader("output/test"))
  // //    reader.readNext() // Skip headers

  //     val batches = reader.readAll.map { row =>
  //       Batch.newBuilder()
  //         .setBatchDateEpoch(dateFormat.parse(row(1)).getTime)
  //         .setBatchType(row(2))
  //         .setDescription(row(3))
  //         .build()
  //     }


  //     val genotypes = reader.readAll.map { row =>
  //       Genotype.newBuilder()
  //         .build()
  //     }

}
