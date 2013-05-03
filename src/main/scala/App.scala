import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._


  //class MyRec(myRec:MyRecord) extends AnyRef {}
  case class MyRec extends AnyRef 
object Main extends App {
  type MyRecord = AnyRef{val x: String} 
  //type MyRecord = {MyRec.asInstanceOf[MyRecord]}
  

  val model$ = DynamicClassLoader.loadClass("models.MyRecord$", MyRecord$Dump.dump()) //Load the module class
  val model  = DynamicClassLoader.loadClass("models.MyRecord", MyRecordDump.dump()) //Then load the class
  val ctor$  = model$.getConstructor()
  val instance$ = ctor$.newInstance()//Change Module$Dump <init> from PRIVATE to PUBLIC, else no method found

  //val instance$ = model$.newInstance()
  val apply$ = model$.getMethod("apply", classOf[String])
  val record =  apply$.invoke(instance$, "hello world").asInstanceOf[CaseClass with Product]
  //val record =  apply$.invoke(instance$, "hello world").asInstanceOf[MyRecord]


 // println(returnType)
//type p <: scala.Product
  //val hello = MyRecord("helloworld")

  val dbo = grater[MyRecord].asDBObject(record)

 // def dbo[MyRecord](rec: MyRecord) = grater[MyRecord].asDBObject(record)
 // def dbo[MyRecord](rec: MyRecord) = grater[AnyRef with Product].asDBObject(record.asInstanceOf[AnyRef with Product])
   //val dbo = grater[MyRecord].asDBObject(record)
 
 println(dbo(record))
  //println()

}

