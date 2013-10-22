//  val mySig = new ScalaSig(List("case class"), List("models", "MyRecord_AllDatatypes"), List(("a", "Byte"), ("b", "Short"), ("c", "Int"), ("d", "Long"), ("e", "Float"), ("f", "Double"), ("g", "Char"), ("h", "String"), ("i", "Boolean"), ("j", "Unit"), ("k", "Null"), ("l", "Nothing"), ("m", "Any"), ("n", "Byte"), ("o", "Object")))


package avocet
import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._


import scala.tools.scalap.scalax.rules.scalasig._

import java.util.Arrays
import scala.reflect.internal.pickling._

import org.specs2._
import mutable._
import specification._

class AllDatatypesSpec extends mutable.Specification {

//usually we'd be reading from a source
 // val infile = new File("input.avro")
 // val typeTemplate = CaseClassGenerator.parseFromFile(infile)//instantiated module class

//but for now lets make it easy debug my Scala signatures 
//All datatypes except Nothing and Null, simply because we're testing with salat, while I can't get an instance of an object witha Nothing field, and salat can't deserialize Nulls
  val valueMembers: List[FieldSeed] = List(FieldSeed("a", "Byte"), FieldSeed("b", "Short"), FieldSeed("c", "Int"), FieldSeed("d", "Long"), FieldSeed("e", "Float"), FieldSeed("f", "Double"), FieldSeed("g", "Char"), FieldSeed("h", "String"), FieldSeed("i", "Boolean"), FieldSeed("j", "Unit"), FieldSeed("m", "Any"), FieldSeed("n", "Byte"), FieldSeed("o", "Object"))

  val classData = ClassData("models", "MyRecord_AllDatatypesSpec", valueMembers, FieldMatcher.getReturnTypes(valueMembers))
  val dcc = new DynamicCaseClass(classData)
//  val module = dcc.model

  val typeTemplate = dcc.instantiated$

  type MyRecord = typeTemplate.type

  val dbo = grater[MyRecord].asDBObject(typeTemplate)
   // println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
 "given a dynamically generated case class MyRecord_StringIntBooleanSpec(a: Int) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      typeTemplate === obj
    }
}



}
