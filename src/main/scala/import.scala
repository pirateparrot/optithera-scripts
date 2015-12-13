import au.com.bytecode.opencsv._
import java.io._

object Hi {
  def main(args: Array[String]) =
    println("Hi!")
    val reader = new CSVReader(new FileReader("output/batches.tsv"))
    println(reader.readNext.mkString("*"))
}
