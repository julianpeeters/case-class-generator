

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._


import org.specs2._
import mutable._
import specification._

class NullSpec extends mutable.Specification {

  val valueMembers: List[FieldSeed] = List(FieldSeed("a","Null"))
  val classData = ClassData(Some("models"), "MyRecord_NullSpec", valueMembers)

  val dcc = new DynamicCaseClass(classData)

  val typeTemplate = dcc.runtimeInstance

  type MyRecord = typeTemplate.type


  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    println(dbo)

 // val obj = grater[MyRecord].asObject(dbo)
  //  println(obj)
 
 "given a dynamically generated case class MyRecord(a: Null) as a type parameter, a grater" should {
    "serialize correctly" in {
      dbo.toString === """{ "a" :  null }"""
    }
}



}
