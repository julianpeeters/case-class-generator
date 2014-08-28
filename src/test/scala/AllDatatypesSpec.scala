import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._



import org.specs2._
import mutable._
import specification._

class AllDatatypesSpec extends mutable.Specification {

  val valueMembers: List[FieldSeed] = List(FieldSeed("a", "Byte"), FieldSeed("b", "Short"), FieldSeed("c", "Int"), FieldSeed("d", "Long"), FieldSeed("e", "Float"), FieldSeed("f", "Double"), FieldSeed("g", "Char"), FieldSeed("h", "String"), FieldSeed("i", "Boolean"), FieldSeed("j", "Unit"), FieldSeed("m", "Any"), FieldSeed("n", "Byte"), FieldSeed("o", "Object"))

  val classData = ClassData(Some("models"), "MyRecord_AllDatatypesSpec", valueMembers)
  val dcc = new DynamicCaseClass(classData)

  val typeTemplate = dcc.runtimeInstance

  type MyRecord = typeTemplate.type

  val dbo = grater[MyRecord].asDBObject(typeTemplate)

  val obj = grater[MyRecord].asObject(dbo)
 
 "given a dynamically generated case class MyRecord_StringIntBooleanSpec(a: Int) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
      typeTemplate === obj
    }
}



}
