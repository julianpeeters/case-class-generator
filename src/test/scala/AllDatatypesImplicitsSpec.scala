import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._ 
import scala.reflect.runtime.universe._

import org.specs2._
import mutable._
import specification._

class AllDatatypesImplicitsSpec extends mutable.Specification {



  
  "given a dynamically generated case class with all datatypes as a type parameter defined as a type alias, a grater" should {
    "serialize and deserialize correctly" in {


    val valueMembersA: List[FieldData] = List(FieldData("i", typeOf[Int]))
    val classDataA = ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_UserA"), ClassFieldData(valueMembersA))
    val dccA = new DynamicCaseClass(classDataA)
    val typeTemplateA = dccA.newInstance(1)

    val valueMembers: List[FieldData] = List(FieldData("a", typeOf[Byte]), FieldData("b", typeOf[Short]), FieldData("c", typeOf[Int]), FieldData("d", typeOf[Long]), FieldData("e", typeOf[Float]), FieldData("f", typeOf[Double]), FieldData("g", typeOf[Char]), FieldData("h", typeOf[String]), FieldData("i", typeOf[Boolean]), FieldData("j", typeOf[Unit]), FieldData("m", typeOf[Any]), FieldData("n", typeOf[Byte]), FieldData("o", typeOf[Object]), FieldData("p", typeOf[List[Int]]), FieldData("q", typeOf[Option[String]]), FieldData("r", dccA.tpe))

    val classData = ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_AllDatatypesImplicitSpec"), ClassFieldData(valueMembers))
    val dcc = new DynamicCaseClass(classData)
 
    val record = dcc.runtimeInstance

    import dcc.implicits._
    type MyRecord = dcc.TYPE

    val dbo = grater[MyRecord].asDBObject(dcc.runtimeInstance)

    val obj = grater[MyRecord].asObject(dbo)



      dcc.runtimeInstance === obj
    }
 }
}
