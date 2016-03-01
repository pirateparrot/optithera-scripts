import au.com.bytecode.opencsv._
import java.io._
import scala.util.Try
import scala.collection.JavaConversions._
import org.bdgenomics.formats.avro._

object Importer {
  def main(args: Array[String]) =
    println("Hello World!")

    val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")

    val reader = new CSVReader(new FileReader("output/test"))
//    reader.readNext() // Skip headers

    val batches = reader.readAll.map { row =>
      Batch.newBuilder()
        .setBatchDateEpoch(dateFormat.parse(row(1)).getTime)
        .setBatchType(row(2))
        .setDescription(row(3))
        .build()
    }

    val phenotypes = reader.readAll.map { row =>
      Phenotype.newBuilder()
        .setPhenotypeType(row(1))
        .setPhenotypeGroup(row(2))
        .setName(row(3))
        .setMeasureDataType(MeasureDataType.valueOf(row(4)))
        .setMeasure(row(5))
        .setMeasureUnits(row(6))
        .setDescription(row(7))
        .setDiagnosisDateEpoch(dateFormat.parse(row(8)).getTime)
        .build()
    }

    val visits = reader.readAll.map { row =>
      Visit.newBuilder()
        .setVisitId(row(0))
        .setVisitDateEpoch(dateFormat.parse(row(1)).getTime)
        .setStudyId(row(2))
        .setIsFasting(Try(java.lang.Boolean.valueOf(row(3))).getOrElse(false))
        .setDescription(row(4))
        .setPhenotypes(phenotypes)
        .build()
    }

    val genotypes = reader.readAll.map { row =>
      Genotype.newBuilder()
        .build()
    }

    val individuals = reader.readAll.map { row =>
      val gender = if (row(5).charAt(0) == 'm') "Male" else if (row(5).charAt(0) == 'f') "Female" else "Unkown"

      Individual.newBuilder()
        .setGenotypes(genotypes)
        .setGender(Gender.valueOf(gender))
        .setIndividualId(row(0))
        .setFamilyId(row(1))
        .setFatherId(row(2))
        .setMotherId(row(3))
        .setBirthDateEpoch(dateFormat.parse(row(4)).getTime)
        .setEthnicCode(row(6))
        .setCentreName(row(7))
        .setRegion(row(8))
        .setCountry(row(9))
        .setNotes(row(10))
        .setVisits(visits)
        .setBatches(batches)
        .build()
    }



}
