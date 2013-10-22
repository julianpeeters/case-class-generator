
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

class StringIntBooleanSpec extends mutable.Specification {

//usually we'd be reading from a source
 // val infile = new File("input.avro")
 // val typeTemplate = CaseClassGenerator.parseFromFile(infile)//instantiated module class

//but for now lets make it easy debug my Scala signatures 
  val valueMembers: List[FieldSeed] = List(FieldSeed("x","String"), FieldSeed("y","Int"), FieldSeed("z","Boolean"))//, FieldSeed("e","Int"), FieldSeed("f","Int"), FieldSeed("g","Int"))
  val classData = ClassData("models", "MyRecord_StringIntBooleanSpec", valueMembers, FieldMatcher.getReturnTypes(valueMembers))
  val dcc = new DynamicCaseClass(classData)
//  val module = dcc.model

  val typeTemplate = dcc.instantiated$

  type MyRecord = typeTemplate.type

//println(classOf[MyRecord])//error: class type required but avocet.Main.typeTemplate.type found
  val parser = ScalaSigParser.parse(dcc.model.getClass)
    println(parser)

  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
 "given a dynamically generated case class MyRecord_StringIntBooleanSpec(a: Int) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      typeTemplate === obj
    }
}



}
