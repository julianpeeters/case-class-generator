//Semi- Useless Test: shows that my class passes the verifier, but java.lang.Object will be a custom class, and will be a case class model with a toString that works
package avocet
import caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._


import scala.tools.scalap.scalax.rules.scalasig._

import java.util.Arrays
import scala.reflect.internal.pickling._

import org.specs2._
import mutable._
import specification._

class ObjectObjectSpec extends mutable.Specification {

//usually we'd be reading from a source
 // val infile = new File("input.avro")
 // val typeTemplate = CaseClassGenerator.parseFromFile(infile)//instantiated module class

//but for now lets make it easy debug my Scala signature issue (chokes on > 3 fields even tho sig bytes are ok before encoding)
  val valueMembers: List[FieldSeed] = List(FieldSeed("a","Object"), FieldSeed("b","Object"))//, FieldSeed("d","Boolean"))
  val classData = ClassData(Some("models"), "MyRecord_ObjectObjectSpec", valueMembers)
  val dcc = new DynamicCaseClass(classData)
//  val module = dcc.model

  val typeTemplate = dcc.instantiated$

  type MyRecord = typeTemplate.type

  val dbo = grater[MyRecord].asDBObject(typeTemplate)
   // println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)

 "given a dynamically generated case class MyRecord(a: Object, b: Object) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      typeTemplate === obj

    }
}



}
