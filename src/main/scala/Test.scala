import com.julianpeeters.caseclass.generator._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import com.gensler.scalavro.types.AvroType

object Test extends App {

  val valueMembers: List[FieldSeed] = List(FieldSeed("x", "String"), FieldSeed("y", "Int"), FieldSeed("z", "Boolean"))
  val classData = ClassData(Some("models"), "B", valueMembers)
  val dcc = new DynamicCaseClass(classData)

  val typeTemplate = dcc.runtimeInstance
  println(scala.reflect.runtime.universe.typeOf[dcc.TYPE].map(_.normalize))
  //  type MyRecord = dcc.TYPE//typeTemplate.type
  type MyRecord = dcc.TYPE  //typeTemplate.type

 // println("xxxxxxxxxx" + scala.reflect.runtime.universe.runtimeMirror(getClass.getClassLoader).runtimeClass(dcc.runtimeInstance.getClass))

 // println("oxoxoxxoxox" + scala.reflect.runtime.universe.runtimeMirror(getClass.getClassLoader).runtimeClass(dcc.tpe))

  //println(AvroType[Test.dcc.dummy.T].schema)
  //println(AvroType[Test.dcc.dummy.T])
 // println(AvroType[MyRecord])
  //println(AvroType[dcc.dummy.T].schema)

  val dbo = grater[MyRecord].asDBObject(typeTemplate.asInstanceOf[MyRecord])
    println(dbo)

   val obj = grater[MyRecord].asObject(dbo)
     println(obj)
     println(dbo == obj)

}
