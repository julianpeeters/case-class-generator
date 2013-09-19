package avocet
import models._

class DynamicCaseClass(classSeed: ClassData) {
//val classDescriptors = ClassData()
    val fullName = (classSeed.classNamespace + "." + classSeed.className)
    val fieldData: List[FieldData] = classSeed.classFields
 //object MyRecord {
    val myRecordDump = new MyRecordDump 
    val bytecodes = myRecordDump.dump(classSeed)
    val model  = DynamicClassLoader.loadClass(fullName, bytecodes(0)) // load the class
    val model$ = DynamicClassLoader.loadClass(fullName + "$", bytecodes(1)) //load the module class

      val methodParams: List[Class[_]] = List(classOf[String], classOf[Int], classOf[Boolean])
      val method_apply = model$.getMethod("apply", methodParams: _*)//populate the instance with values,
      val instance$ = model$.getConstructor().newInstance()//getInstance(module)
 //     val insantiationParams: List[Object] = List("", 1.asInstanceOf[Object], true.asInstanceOf[Object] )
      val insantiationParams: List[Object] = List("", 1.asInstanceOf[Object], true.asInstanceOf[Object] )
     // method_apply.invoke(instance, insantiationParams: _*)
   // }
   // val typeTemplate = MyRecord("hello runtime") //Rename def to MyRecord to preserve the look and feel of salat??
  //  val typeTemplate = MyRecord("hello runtime", 1, true) //Rename def to MyRecord to preserve the look and feel of salat??
    val instantiated$ = model$.getMethod("apply", methodParams: _*).invoke(instance$,  insantiationParams: _*)

}




