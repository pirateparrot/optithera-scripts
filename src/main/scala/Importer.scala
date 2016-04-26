import com.opencsv._
import java.io._
import scala.collection.JavaConversions._
import org.bdgenomics.formats.avro._
import scala.collection.parallel._

object Importer {

  def main(args: Array[String]) = {

    if (args.size != 2) {
      println("Passed arguments were: " + args.mkString(", "))
      println("Arguments must be: genotypeVersion(v1, v2) nbThreads")
      sys.exit(0)
    }
    val genotypeVersion = args(0)
    val nbThreads = args(1).toInt

    val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")
    val prefix = "./output/"
    val batchData = new collection.mutable.HashMap[String,Batch]()
    // var writeToParquet = scala.collection.mutable.Buffer[Individual]()
    // val parquetWriter = new ParquetWriter[Individual](writeToParquet, "./parquet/individuals", Individual.getClassSchema)

    // Warm up Spark
    val sc = AppContext.sc

    // Load the batches first
    val reader = new CSVIterator(new CSVReader(new FileReader(prefix + "Batches.tsv")))
    reader.next() // Skip headers
    reader.foreach { row =>
      val batch = Batch.newBuilder()
      batch.setBatchDateEpoch(dateFormat.parse(row(1)).getTime)
      batch.setBatchType(row(2))
      batch.setDescription(row(3))
      batchData += row(0) -> batch.build()
    }

    // Then everything else
    val handler = new DirectoryHandler(prefix, genotypeVersion, batchData, dateFormat)
    val list = new java.io.File(prefix).listFiles.sortWith(_.getName < _.getName).toList
    val parallelList = list.filter(f => f.isDirectory).par
    parallelList.tasksupport = new ForkJoinTaskSupport(new scala.concurrent.forkjoin.ForkJoinPool(nbThreads))
    val nbWritten = parallelList.map({ file =>
      val id = file.getName
      handler.processIndividual(id) match {
        case (Some(writable)) => {
          val buffer = scala.collection.mutable.Buffer[Individual](writable)
          val parquetFile = "./parquet/individual_" + genotypeVersion + "_" + id
          val parquetWriter = new ParquetWriter[Individual](buffer, parquetFile, Individual.getClassSchema)
          parquetWriter.persistData
          Some()
        }
        case (None) => None
      }
    }).flatten

    // Disconnect
    sc.stop

    println(nbWritten.size + " individuals loaded")

  }
}
