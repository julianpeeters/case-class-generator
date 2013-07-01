import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

object Main extends App {

  def createRecord(fieldVal: String) = {
    val model  = DynamicClassLoader.loadClass("models.MyRecord", MyRecordDump.dump().get(0)) // load the class
    val model$ = DynamicClassLoader.loadClass("models.MyRecord$", MyRecordDump.dump().get(1)) //load the module class
    applyValues(model$, fieldVal) //get an instance
  }

  def applyValues(classModule: java.lang.Class[_], fieldValue: String) = {
    val method_apply = classModule.getMethod("apply", classOf[String])//populate the instance with values,
    val classInstance = classModule.getConstructor().newInstance()//getInstance(module)
    method_apply.invoke(classInstance, fieldValue)//roughly equivalent to using the statement `MyRecord(fieldValue)`
  }


  val record = createRecord("hello world") //Rename def to MyRecord to preserve the look and feel of salat??
  type MyRecord = record.type
  
  val dbo = grater[MyRecord].asDBObject(record)

    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
  println(record == obj)
 
}




