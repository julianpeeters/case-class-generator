import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

object Main extends App {

  object MyRecord {
    val myRecordDump = new MyRecordDump 
    val bytecodes = myRecordDump.dump
    val model  = DynamicClassLoader.loadClass("models.MyRecord", bytecodes(0)) // load the class
    val model$ = DynamicClassLoader.loadClass("models.MyRecord$", bytecodes(1)) //load the module class
/*
//    def apply(fieldValue: String, y: Int, z: Boolean) = {


      val method_apply = model$.getMethod("apply", classOf[String])//, classOf[Int], classOf[Boolean])//populate the instance with values,
      val classInstance = model$.getConstructor().newInstance()//getInstance(module)
      method_apply.invoke(classInstance, fieldValue)//, y: java.lang.Integer, z: java.lang.Boolean)//roughly equivalent to using the statement `MyRecord(fieldValue)`
    }
*/

    def apply(fieldValue: Any) = {
      val method_apply = model$.getMethod("apply", fieldValue.getClass)//populate the instance with values,
      val classInstance = model$.getConstructor().newInstance()//getInstance(module)
      method_apply.invoke(classInstance, fieldValue.asInstanceOf[Object])
    }
    val typeTemplate = MyRecord("hello runtime") //Rename def to MyRecord to preserve the look and feel of salat??
  //  val typeTemplate = MyRecord("hello runtime", 1, true) //Rename def to MyRecord to preserve the look and feel of salat??
    type MyRecord = typeTemplate.type
  }

  
  val dbo = grater[MyRecord.MyRecord].asDBObject(MyRecord.typeTemplate)

    println(dbo)

  val obj = grater[MyRecord.MyRecord].asObject(dbo)
    println(obj)
 
//  println(typeTemplate == obj)
   println(MyRecord.typeTemplate == obj)
}




