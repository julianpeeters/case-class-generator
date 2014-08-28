package com.julianpeeters.caseclass.generator

class DynamicCaseClass(classData: ClassData) {

  //prepare the input as to improve readability of the code
  val name = classData.className

  val namespace : Option[String]= { 
    if (classData.classNamespace.isDefined) Some(classData.classNamespace.get.replaceAllLiterally("/", "."))
    else None
  }

  val fullName = { 
    if (namespace.isDefined) (namespace.get + "." + name)
    else name
  }

  val fieldData: List[FieldSeed] = classData.classFields

  //using data from the input, prepare the arguments for reflective instantiation of the yet-to-be-gen'd class
  val methodParams: List[Class[_]] = FieldMatcher.getReturnType(fieldData).asInstanceOf[List[Class[_]]] 
  val instantiationParams: List[Object] = fieldData.map(fd => FieldMatcher.getTypeData(namespace, fd.fieldType).asParam)

  //get an ASM classwriter, passing it the data needed to dynamically generate a class, "dump" the bytecode
  val bytecode =  BytecodeGenerator.dump(classData)

  val runtimeClass: java.lang.Class[_]  = DynamicClassLoader.loadClass(fullName, bytecode(0)) //load the class
  val companionClass: java.lang.Class[_] = DynamicClassLoader.loadClass(fullName + "$", bytecode(1)) //load the module class

  //now that we've loaded our classes, use reflection to get an instance of the companion class
  val method_apply = companionClass.getMethod("apply", methodParams: _*)//populate the instance values
  val companionCtor = companionClass.getConstructor()
  val runtimeCompanionObject = companionCtor.newInstance()//get an instance of the companion
  val runtimeInstance: java.lang.Object = method_apply.invoke(runtimeCompanionObject, instantiationParams: _*)
  type Type = runtimeInstance.type

  //After a DynamicCaseClass is made, add it to the list of generated Classes 
  ClassStore.accept(this)
}




