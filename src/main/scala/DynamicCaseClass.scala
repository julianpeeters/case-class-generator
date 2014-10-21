package com.julianpeeters.caseclass.generator

class DynamicCaseClass(classData: ClassData) {

  //prepare the input as to improve readability of the code
  val name = classData.className

  val namespace: Option[String] = {
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
  val bytecode = BytecodeGenerator.dump(classData)
  val runtimeClass: java.lang.Class[_] = DynamicClassLoader.loadClass(fullName, bytecode(0)) //load the class
  val companionClass: java.lang.Class[_] = DynamicClassLoader.loadClass(fullName + "$", bytecode(1)) //load the module class

  //now that we've loaded our classes, use reflection to get an instance of the companion class
  val method_apply = companionClass.getMethod("apply", methodParams: _*) //populate the instance values
  val companionCtor = companionClass.getConstructor()
  val runtimeCompanionObject = companionCtor.newInstance() //get an instance of the companion
  val runtimeInstance: java.lang.Object = method_apply.invoke(runtimeCompanionObject, instantiationParams: _*)
  //type Type = runtimeInstance.type

  // adapted from http://stackoverflow.com/questions/22970209/get-typetaga-from-classa/22972751#22972751
  import scala.reflect.runtime.universe._
  import scala.reflect.api._
  //  val mirror = runtimeMirror(runtimeInstance.getClass.getClassLoader) // obtain runtime mirror
  val mirror = runtimeMirror(runtimeClass.getClassLoader) // obtain runtime mirror
  //  val sym = mirror.staticClass(runtimeInstance.getClass.getName) // obtain class symbol for `c`
  println(runtimeClass.getName)

  val sym = mirror.staticClass(runtimeClass.getName) // obtain class symbol for `c`
  println(sym)
  val tpe = sym.selfType // obtain type object for `c`
  println("tpe " + tpe)
  println(tpe.typeSymbol.asClass)


  // create a type tag which contains above type object
  val tt = TypeTag(mirror, new TypeCreator {
    def apply[U <: Universe with Singleton](m: scala.reflect.api.Mirror[U]) =
      if (m eq mirror) tpe.asInstanceOf[U#Type]
      else throw new IllegalArgumentException(s"Type tag defined in $mirror cannot be migrated to other mirrors.")
  })

  class TypeCheckDummy {
    //case class T 
    type T = AnyRef 
    implicit val tag: TypeTag[T] = tt.asInstanceOf[TypeTag[T]]
//    implicit val manifest: Manifest[TYPE] = Manifest.classType(runtimeClass)
    implicit val manifest: Manifest[T] = Manifest.classType(runtimeClass)

  }

  val dummy = new TypeCheckDummy;
  import dummy._

  type TYPE = T 

 // println("ooooooooo" + typeOf[T].typeSymbol.asClass)

  //After a DynamicCaseClass is made, add it to the list of generated Classes 
  ClassStore.accept(this)
}

