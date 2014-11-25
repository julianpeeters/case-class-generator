//TODO Unit is not yet supported as a field member's type

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._ 
import scala.reflect.runtime.universe._



import org.specs2._
import mutable._
import specification._

class UnitSpec extends mutable.Specification {



  val valueMembers: List[FieldData] = List(FieldData("a", typeOf[Unit]))//, FieldData("b", typeOf[Int]))//, FieldData("d", typeOf[Boolean]))
  val classData = ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_UnitSpec"), ClassFieldData(valueMembers))
  val dcc = new DynamicCaseClass(classData)


  val typeTemplate = dcc.newInstance(())

  type MyRecord = typeTemplate.type



  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    //println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
 "given a dynamically generated case class MyRecord_UnitSpec(a: Unit) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      typeTemplate === obj
    }
}



}
