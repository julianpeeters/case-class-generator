import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
//import org.objectweb.asm._
import java.lang.reflect._
import scala.reflect.runtime.universe._
import scala.reflect.runtime._

  //class MyRec(myRec:MyRecord) extends AnyRef {}
  case class MyRec extends AnyRef 
object Main extends App {
  type MyRecord = {val x: String} 
  //type MyRecord = {MyRec.asInstanceOf[MyRecord]}
  

  val model$ = DynamicClassLoader.loadClass("models.MyRecord$", MyRecord$Dump.dump()) //Load the module class
  val model  = DynamicClassLoader.loadClass("models.MyRecord", MyRecordDump.dump()) //Then load the class
  val ctor$  = model$.getConstructor()
  val instance$ = ctor$.newInstance()//Change Module$Dump <init> from PRIVATE to PUBLIC, else no method found

  //val instance$ = model$.newInstance()
  val apply$ = model$.getMethod("apply", classOf[String])
  val record =  apply$.invoke(instance$, "hello world").asInstanceOf[CaseClass]
  //val record =  apply$.invoke(instance$, "hello world").asInstanceOf[{val x: String}]
  //val record =  apply$.invoke(instance$, "hello world").asInstanceOf[MyRecord]
 
   // println(record.x)


   println(typeOf[MyRecord])

//println(record.isInstanceOf[AnyRef])
//println(record.getClass)
 // val myRecord = record.asInstanceOf[MyRec]
 // println(myRecord)


class Dog{
  def bark = println("bow wow")
  def wag = println(":)")   
}
def dogit(c:{def bark}) = c.bark
dogit(new Dog)


 // println(newRec)
  
 // val method = model$.getMethod("apply", MyRecord.getClass())
 // val returnType = method.getGenericReturnType
//  println(method)

 // println(returnType)
//type p <: scala.Product
  //val hello = MyRecord("helloworld")

 // val dbo = grater[MyRecord].asDBObject(hello)
  //val dbo = grater[MyRecord].asDBObject(record)
 // val dbo = grater[MyRecord].asDBObject(record.asInstanceOf[MyRecord])
//val dbo = grater[MyRecord with Product with AnyRef].asDBObject(record)//Needed AnyRef: struct.type is Object
 //
//val dbo = grater[MyRecord].asDBObject(record.asInstanceOf[MyRecord with Product])//Needed AnyRef: struct.

//val dbo = grater[MyRecord with CaseClass].asDBObject(record.asInstanceOf[MyRecord with CaseClass])//Needed AnyRef: struct. type is Object
 //val dbo = grater[AnyRef with Product].asDBObject(record.asInstanceOf[AnyRef with Product])
 

 // def dbo[MyRecord](rec: MyRecord) = grater[MyRecord].asDBObject(record)
  def dbo[MyRecord](rec: MyRecord) = grater[AnyRef with Product].asDBObject(record.asInstanceOf[AnyRef with Product0])
   //val dbo = grater[MyRecord].asDBObject(record)
 
 println(dbo(record.asInstanceOf[AnyRef with Product]))
  //println()
  
 // val t = Type.getType(model$)
//  type ty = t.type
 // val dbo = grater[ty]//.asDBObject(t)
  //println()
}
/*
private class asClass {
  val model$ = DynamicClassLoader.loadClass("models.MyRecord$", MyRecord$Dump.dump()) //Load the object
  val model  = DynamicClassLoader.loadClass("models.MyRecord", MyRecordDump.dump()) //Then load the class
}
*/
