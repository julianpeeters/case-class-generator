

import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._ 
import scala.reflect.runtime.universe._



import org.specs2._
import mutable._
import specification._

class IntSpec extends mutable.Specification {


 
 "given a dynamically generated case class MyRecord_IntSpecs(a: Int) as a type parameter, a grater" should {
    "serialize and deserialize correctly" in {
println("Spec testing class")



  val valueMembers: List[FieldData] = List(FieldData("a", typeOf[Int]))
  val classData = ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_IntSpec"), ClassFieldData(valueMembers))
println("Spec making class")
  val dcc = new DynamicCaseClass(classData)
println("Spec made class")

  val typeTemplate = dcc.newInstance(1)

  type MyRecord = typeTemplate.type


  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
      typeTemplate === obj
    }
}

println("Spec tested class")

}
