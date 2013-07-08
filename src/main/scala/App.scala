import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

object Main extends App {

  object MyRecord { 
    val model  = DynamicClassLoader.loadClass("models.MyRecord", MyRecordDump.dump().get(0)) // load the class
    val model$ = DynamicClassLoader.loadClass("models.MyRecord$", MyRecordDump.dump().get(1)) //load the module class

    def apply(fieldValue: String) = {
      val method_apply = model$.getMethod("apply", classOf[String])//populate the instance with values,
      val classInstance = model$.getConstructor().newInstance()//getInstance(module)
      method_apply.invoke(classInstance, fieldValue)//roughly equivalent to using the statement `MyRecord(fieldValue)`
    }
    val typeTemplate = MyRecord("hello world") //Rename def to MyRecord to preserve the look and feel of salat??
    type MyRecord = typeTemplate.type
  }

  
  val dbo = grater[MyRecord.MyRecord].asDBObject(MyRecord.typeTemplate)

    println(dbo)

  val obj = grater[MyRecord.MyRecord].asObject(dbo)
    println(obj)
 
//  println(typeTemplate == obj)
   println(MyRecord.typeTemplate == obj)
}




