
package com.julianpeeters.avro.runtime.provider

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._ 
import scala.reflect.runtime.universe._
import org.specs2._
import mutable._
import specification._


class UserDefinedSpec extends mutable.Specification {


  val valueMembersA: List[FieldData] = List(FieldData("i", typeOf[Int]))
  val classDataA = new ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_UserDefinedRefSpecA"), ClassFieldData(valueMembersA))
  val dccA = DynamicCaseClass(classDataA)
  val typeTemplateA = dccA.newInstance(1)

  val valueMembersB: List[FieldData] = List(FieldData("a", dccA.tpe))
  val classDataB = new ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_UserDefinedRefSpecB"), ClassFieldData(valueMembersB))
  val dccB = DynamicCaseClass(classDataB)
  val typeTemplateB = dccB.newInstance(typeTemplateA)

  val valueMembersC: List[FieldData] = List(FieldData("a", dccA.tpe), FieldData("b", dccB.tpe))
  val classDataC = new ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_UserDefinedRefSpecC"), ClassFieldData(valueMembersC))
  val dccC = DynamicCaseClass(classDataC)
  val typeTemplateC = dccC.newInstance(typeTemplateA, typeTemplateB)

  import dccC.implicits.{tag, manifest}

  val dbo = grater[dccC.TYPE].asDBObject(typeTemplateC)
    println("dbo: "  + dbo)

  val obj = grater[dccC.TYPE].asObject(dbo)
   println("obj: " + obj)

  "given a dynamically generated case class MyRecord_UserDefinedRefSpecC(a: MyRecord_UserDefinedRefSpecB, b: MyRecord_UserDefinedRefSpecB) as a type parameter, a grater" should {"serialize and deserialize correctly" in {

    obj === typeTemplateC
    }
  }
}

