

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._




import org.specs2._
import mutable._
import specification._

class AnyRefSpec extends mutable.Specification {

  val valueMembers: List[FieldData] = List(FieldData("a","AnyRef"))//, FieldData("b","Int"))
  val classData = ClassData(Some("models"), "MyRecord_AnyRefSpec", valueMembers)
  val dcc = new DynamicCaseClass(classData)


  val typeTemplate = dcc.runtimeInstance

  type MyRecord = typeTemplate.type


  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
 "given a dynamically generated case class MyRecord_AnyRefSpec(c: AnyRef) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      typeTemplate === obj
    }
}



}
