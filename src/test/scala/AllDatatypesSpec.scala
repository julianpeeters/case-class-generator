import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import org.specs2._
import mutable._
import specification._

class AllDatatypesSpec extends mutable.Specification {

  val valueMembers: List[FieldData] = List(FieldData("a", "Byte"), FieldData("b", "Short"), FieldData("c", "Int"), FieldData("d", "Long"), FieldData("e", "Float"), FieldData("f", "Double"), FieldData("g", "Char"), FieldData("h", "String"), FieldData("i", "Boolean"), FieldData("j", "Unit"), FieldData("m", "Any"), FieldData("n", "Byte"), FieldData("o", "Object"))

  val classData = ClassData(Some("models"), "MyRecord_AllDatatypesSpec", valueMembers)
  val dcc = new DynamicCaseClass(classData)
 
  val record = dcc.runtimeInstance
  
  type MyRecord = dcc.runtimeInstance.type

  val dbo = grater[MyRecord].asDBObject(dcc.runtimeInstance)

  val obj = grater[MyRecord].asObject(dbo)
 
 "given a dynamically generated case class with all datatypes as a type parameter defined as a type alias, a grater" should {
    "serialize and deserialize correctly" in {
      dcc.runtimeInstance === obj
    }
}



}
