import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import org.specs2._
import mutable._
import specification._

class StringIntBooleanSpec extends mutable.Specification {

  val valueMembers: List[FieldSeed] = List(FieldSeed("x","String"), FieldSeed("y","Int"), FieldSeed("z","Boolean"))
  val classData = ClassData(Some("models"), "MyRecord_StringIntBooleanSpec", valueMembers)
  val dcc = new DynamicCaseClass(classData)

  val typeTemplate = dcc.runtimeInstance

  type MyRecord = typeTemplate.type

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
