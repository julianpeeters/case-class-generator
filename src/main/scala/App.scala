
package avocet
import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._


import scala.tools.scalap.scalax.rules.scalasig._

import scala.reflect.internal.pickling._

import com.novus.salat.annotations.util._
import scala.reflect.ScalaSignature
//Main is for debugging purposes
object Main extends App {
//usually we'd be reading from a source
 // val infile = new File("input.avro")
 // val typeTemplate = CaseClassGenerator.parseFromFile(infile)//instantiated module class

//but for now lets feed the class info in manually
 // val valueMembers: List[FieldSeed] = List(FieldSeed("x","Long"), FieldSeed("y","Long"))//, FieldSeed("z","Boolean"))
  val valueMembers: List[FieldSeed] = List(FieldSeed("a", "Byte"), FieldSeed("b", "Short"), FieldSeed("c", "Int"), FieldSeed("d", "Long"), FieldSeed("e", "Float"), FieldSeed("f", "Double"), FieldSeed("g", "Char"), FieldSeed("h", "String"), FieldSeed("i", "Boolean"), FieldSeed("j", "Float"), FieldSeed("m", "Any"), FieldSeed("n", "Byte"), FieldSeed("o", "Object"), FieldSeed("p", "Long"), FieldSeed("q", "Float"))

  val classData = ClassData("models", "MyRecord", valueMembers, FieldMatcher.getReturnTypes(valueMembers))
  val dcc = new DynamicCaseClass(classData)

  val typeTemplate = dcc.instantiated$

  type MyRecord = typeTemplate.type

  val dbo = grater[MyRecord].asDBObject(typeTemplate)
  //  println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
  //  println(obj)
 
  println(typeTemplate == obj)
}






