package caseclass.generator

class DynamicCaseClass(classData: ClassData) {

//prepare the input as to improve readability of the code
  val name = classData.className
  //val namespace = classData.classNamespace.replaceAllLiterally(".", "/")
  val namespace = classData.classNamespace.replaceAllLiterally("/", ".")
println("================================" + namespace)
//  val fullName = (namespace + "." + name)
  val fullName = (namespace + "." + name)
println(fullName)

println(fullName)
  val fieldData: List[FieldSeed] = classData.classFields
//println(classData.returnType)
//  println("dcc return type " + classData.returnType.head.getClass)

//using data from the input, prepare the arguments needed for reflective instantiation of the yet-to-be-generated class
  val methodParams: List[Class[_]] = {
//    classData.returnType.asInstanceOf[List[Class[_]]]
     FieldMatcher.getReturnType(fieldData).asInstanceOf[List[Class[_]]]
  } 
    println("methodParams" + methodParams)


  val insantiationParams: List[Object] = fieldData.map(fd => FieldMatcher.getTypeData(namespace, fd.fieldType).asParam)
    println("insantiationParams" + insantiationParams)

//get a new ASM classwriter, passing it the data necessary to dynamically generate a class, and "dump" the bytecode
  val bytecode =  BytecodeGenerator.dump(classData)
  val model  = DynamicClassLoader.loadClass(fullName, bytecode(0)) //load the class
  val model$ = DynamicClassLoader.loadClass(fullName + "$", bytecode(1)) //load the module class

//now that we've loaded our classes with a dynamic classloader, use reflection to get an instance of the companion class
  val method_apply$ = model$.getMethod("apply", methodParams: _*)//populate the instance values
  val instance$ = model$.getConstructor().newInstance()//get an instance of the companion
  val instantiated$ = method_apply$.invoke(instance$,  insantiationParams: _*)

//After a DynamiceCaseClass is made, add it to the list of generated Classes 
  CaseClassGenerator.accept(this)

}




