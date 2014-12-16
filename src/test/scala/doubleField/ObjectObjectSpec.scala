//Semi- Useless Test: shows that my class passes the verifier, but java.lang.Object will be a custom class, and will be a case class model with a toString that works

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._ 
import scala.reflect.runtime.universe._


import org.specs2._
import mutable._
import specification._

class ObjectObjectSpec extends mutable.Specification {



 "given a dynamically generated case class MyRecord(a: Object, b: Object) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {


  val valueMembers: List[FieldData] = List(FieldData("a",typeOf[Object]), FieldData("b",typeOf[Object]))
  val classData = ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_ObjectObjectSpec"), ClassFieldData(valueMembers))
  val dcc = new DynamicCaseClass(classData)


  val typeTemplate = dcc.runtimeInstance

  type MyRecord = typeTemplate.type

  val dbo = grater[MyRecord].asDBObject(typeTemplate)
   // println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)


      typeTemplate === obj

    }
}



}
