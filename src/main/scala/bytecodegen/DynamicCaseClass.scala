package avocet
import models._

class DynamicCaseClass(classData: ClassData) {

//prepare the input as to improve readability of the code
  val fullName = (classData.classNamespace + "." + classData.className)
  val fieldData: List[FieldSeed] = classData.classFields

//using data from the input, prepare the arguments needed for reflective instantiation of the yet-to-be-generated class
  val methodParams: List[Class[_]] = classData.returnType.asInstanceOf[List[Class[_]]]
  val insantiationParams: List[Object] = fieldData.map(f => FieldMatcher.getObject(f.fieldType))

//get a new ASM classwriter, passing it the data necessary to dynamically generate a class, and "dump" the bytecode
  val bytecode = (new CaseClassGenerator).generateBytecode(classData)
  val model  = DynamicClassLoader.loadClass(fullName, bytecode(0)) //load the class
  val model$ = DynamicClassLoader.loadClass(fullName + "$", bytecode(1)) //load the module class

//now that we've loaded our classes with a dynamic classloader, use reflection to get an instance of the companion class
  val method_apply = model$.getMethod("apply", methodParams: _*)//populate the instance with real or dummy values,
  val instance$ = model$.getConstructor().newInstance()//get an instance of the companion
  val instantiated$ = model$.getMethod("apply", methodParams: _*).invoke(instance$,  insantiationParams: _*)
}




