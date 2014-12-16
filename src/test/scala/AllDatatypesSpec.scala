import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._ 
import scala.reflect.runtime.universe._
import org.specs2._
import mutable._
import specification._

class AllDatatypesSpec extends mutable.Specification {


 
  "given a dynamically generated case class with all datatypes as a type parameter defined as a type alias, a grater" should {
    "serialize and deserialize correctly" in {

  val valueMembers: List[FieldData] = List(FieldData("a", typeOf[Byte]), FieldData("b", typeOf[Short]), FieldData("c", typeOf[Int]), FieldData("d", typeOf[Long]), FieldData("e", typeOf[Float]), FieldData("f", typeOf[Double]), FieldData("g", typeOf[Char]), FieldData("h", typeOf[String]), FieldData("i", typeOf[Boolean]), FieldData("j", typeOf[Unit]), FieldData("m", typeOf[Any]), FieldData("n", typeOf[Byte]), FieldData("o", typeOf[Object]), FieldData("p", typeOf[List[Int]]), FieldData("q", typeOf[Option[String]]))

  val classData = new ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_AllDatatypesSpec"), ClassFieldData(valueMembers))
  val dcc = new DynamicCaseClass(classData)
 
  val record = dcc.runtimeInstance
  
  type MyRecord = record.type

  val dbo = grater[MyRecord].asDBObject(record)

  val obj = grater[MyRecord].asObject(dbo)

      record === obj
    }
  }

}
