import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import org.specs2._
import mutable._
import specification._

class TYPESpec extends mutable.Specification {

  val valueMembers: List[FieldData] = List(FieldData("x","String"), FieldData("y","Int"), FieldData("z","Boolean"))
  val classData = ClassData(Some("models"), "MyRecord_TYPESpec", valueMembers)
  val dcc = new DynamicCaseClass(classData)

  type MyRecord =  dcc.TYPE

  import dcc.implicits._

  val dbo = grater[MyRecord].asDBObject(dcc.runtimeInstance)
    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
 "given a dynamically generated case class MyRecord_TYPESpec(a: Int) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      dcc.runtimeInstance === obj
    }
}



}
