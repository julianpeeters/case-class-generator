

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._ 
import scala.reflect.runtime.universe._

import org.specs2._
import mutable._
import specification._

class StringStringSpec extends mutable.Specification {


  val valueMembers: List[FieldData] = List(FieldData("a", typeOf[String]), FieldData("b", typeOf[String]))
  val classData = ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_StringStringSpec"), ClassFieldData(valueMembers))
  val dcc = new DynamicCaseClass(classData)


  val typeTemplate = dcc.runtimeInstance

  type MyRecord = typeTemplate.type


  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
 "given a dynamically generated case class MyRecord_StringStringSpec(a: String, b: String) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      typeTemplate === obj
    }
}



}
