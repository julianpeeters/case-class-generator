

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._ 
import scala.reflect.runtime.universe._



import org.specs2._
import mutable._
import specification._

class NullNullSpec extends mutable.Specification {


 
 "given a dynamically generated case class MyRecord_NullNull(a: Null, b: Null) as a type parameter, a grater" should {
    "serialize correctly" in {


  val valueMembers: List[FieldData] = List(FieldData("a",typeOf[Null]), FieldData("b",typeOf[Null]))
  val classData = ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_NullNullSpec"), ClassFieldData(valueMembers))

  val dcc = new DynamicCaseClass(classData)

  val typeTemplate = dcc.runtimeInstance

  type MyRecord = typeTemplate.type


  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    println(dbo)

  //val obj = grater[MyRecord].asObject(dbo)
  //  println(obj)

      dbo.toString === """{ "a" :  null  , "b" :  null }"""
    }
}



}
