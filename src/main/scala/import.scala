import au.com.bytecode.opencsv._
import java.io._
import org.bdgenomics.formats.avro._

object Importer {
  def main(args: Array[String]) =
    println("Hello World!")
    val reader = new CSVReader(new FileReader("output/batches.tsv"))
    val v = new Variant()
    println(reader.readNext.mkString("*"))
}
