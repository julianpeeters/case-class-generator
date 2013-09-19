package avocet
import models._

class DynamicCaseClass(classSeed: ClassData) {
  val fullName = (classSeed.classNamespace + "." + classSeed.className)
  val fieldData: List[FieldData] = classSeed.classFields
  val methodParams: List[Class[_]] = classSeed.returnType.asInstanceOf[List[Class[_]]]
  val insantiationParams: List[Object] = fieldData.map(f => f.asParam)

  val bytecode = (new MyRecordDump).dump(classSeed)
  val model  = DynamicClassLoader.loadClass(fullName, bytecode(0)) // load the class
  val model$ = DynamicClassLoader.loadClass(fullName + "$", bytecode(1)) //load the module class

  val method_apply = model$.getMethod("apply", methodParams: _*)//populate the instance with values,
  val instance$ = model$.getConstructor().newInstance()//get an instance of the companion
  val instantiated$ = model$.getMethod("apply", methodParams: _*).invoke(instance$,  insantiationParams: _*)
}




