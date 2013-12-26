package avocet


import caseclass.generator._
import com.banno.salat.avro._

//import com.novus.salat._
import global._
//import models._

import java.io.File
import org.apache.avro._



//import scala.tools.scalap.scalax.rules.scalasig._

//import scala.reflect.internal.pickling._

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

//  val infile = new File("enron_head.avro")
  //val infile = new File("input2.avro")
 // val infile = new File("input.avro")
  val infile = new File("twitter.avro")
  val outfile = new File("output.avro")
  val jsonSchema: String = getSchemaAsString(infile)
println("json " + jsonSchema)
  val typeTemplate = CaseClassGenerator.asInstance(jsonSchema).instantiated$//instantiated module class


//but for now lets feed the class info in manually


//val tempDescriptors = List(ClassData("models","MyRecord",List(FieldData("x","rec","Lrec;",ALOAD,ARETURN)),List("rec")), ClassData("models","rec",List(FieldData("i","int","I",ILOAD,IRETURN)),List("int"))).reverse


/*
 // val valueMembers: List[FieldSeed] = List(FieldSeed("x","Long"), FieldSeed("y","Long"), FieldSeed("z","Boolean"))
 // val classData = ClassData("models", "MyRecord", valueMembers)
  val valueMembers: List[FieldSeed] = List(FieldSeed("i","Int"))
  val classData = ClassData("models", "rec", valueMembers)
  val dcc = new DynamicCaseClass(classData)

  val valueMembers2: List[FieldSeed] = List(FieldSeed("x","rec"))
  val classData2 = ClassData("models", "MyRecord", valueMembers2)
  val dcc2 = new DynamicCaseClass(classData2)

  val typeTemplate = dcc2.instantiated$
*/

println("type template " + typeTemplate)
println(typeTemplate.getClass)

  type Record = typeTemplate.type 


println(grater[Record])
  //val dbo = grater[Record].asDBObject(typeTemplate)
  //  println(dbo)


  //val obj = grater[Record].serializeToFile(outfile, typeTemplate)


  val obj = grater[Record].asObjectsFromFile(infile)
    obj foreach println

 
 // println(typeTemplate == obj)
}






