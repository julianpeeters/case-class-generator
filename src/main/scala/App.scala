import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

object Main extends App {

  val model  = DynamicClassLoader.loadClass("models.MyRecord", MyRecordDump.dump()) // load the class
  val model$ = DynamicClassLoader.loadClass("models.MyRecord$", MyRecord$Dump.dump()) //load the module class

  val ctor$  = model$.getConstructor()
  val instance$ = ctor$.newInstance()//Changed Module$Dump <init> from PRIVATE to PUBLIC, else no method found

  val apply$ = model$.getMethod("apply", classOf[String])//populate the instance with values,
  val record = apply$.invoke(instance$, "hello world")//roughly equivalent to using the statement `MyRecord("hello world")`
    println(record)

  val dbo = grater[record.type].asDBObject(record)
    println(dbo)

  val obj = grater[record.type].asObject(dbo)
    println(obj)
  
  println(record == obj)
 
}




