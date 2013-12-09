package avocet
import org.apache.avro._

import caseclass.generator._
import com.banno.salat.avro._
//import com.banno.salat.avro.global._

//import com.novus.salat._
import global._
import com.mongodb.casbah.Imports._
import java.io._
import org.apache.avro._
import org.apache.avro.io._
import org.apache.avro.file._


import scala.tools.scalap.scalax.rules.scalasig._

import scala.reflect.internal.pickling._

//import com.novus.salat.annotations.util._
//import scala.reflect.ScalaSignature



//Main is for debugging purposes
object Main extends App {
//usually we'd be reading from a source


  def getSchemaAsString(infile: java.io.File): String = {
    val bufferedInfile = scala.io.Source.fromFile(infile, "iso-8859-1")
    val parsable = new String(bufferedInfile.getLines.mkString.dropWhile(_ != '{').toCharArray)
    val avroSchema = new Schema.Parser().parse(parsable)
    avroSchema.toString
  }

  //val infile = new File("twitter.avro")
  val infile = new File("input.avro")
  val jsonSchema: String = getSchemaAsString(infile)
  val typeTemplate = CaseClassGenerator.asInstance(jsonSchema)//instantiated module class

//but for now lets feed the class info in manually
 // val valueMembers: List[FieldSeed] = List(FieldSeed("x","Long"), FieldSeed("y","Long"), FieldSeed("z","Boolean"))
//  val classData = ClassData("models", "MyRecord", valueMembers, FieldMatcher.getReturnTypes(valueMembers))

 // val dcc = new DynamicCaseClass(classData)
 // val typeTemplate = dcc.instantiated$

println(typeTemplate)
println(typeTemplate.getClass)

  type Record = typeTemplate.type 
//println(MyRecord.getClass)



 // val obj = grater[MyRecord].asObject(dbo)
  val obj = grater[Record].asObjectsFromFile(infile)
    //println(obj)
    obj foreach println

 
 // println(typeTemplate == obj)
}






