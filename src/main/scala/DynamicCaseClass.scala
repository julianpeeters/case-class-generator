package com.julianpeeters.caseclass.generator

class DynamicCaseClass(classData: ClassData) {

  // Prepare the input as to improve readability of the code
  val name = classData.className

  val namespace: Option[String] = {
	if (classData.classNamespace.isDefined) Some(classData.classNamespace.get.replaceAllLiterally("/", "."))
    else None
  }

  val fullName = {
    if (namespace.isDefined) (namespace.get + "." + name)
    else name
  }

  // Using data from the input, prepare the arguments for reflective instantiation of the yet-to-be-gen'd class
  val methodParams: List[Class[_]] = FieldMatcher.getReturnType(classData.classFields).asInstanceOf[List[Class[_]]]
  val instantiationParams: List[Object] = classData.classFields.map(fd => FieldMatcher.getTypeData(namespace, fd.fieldType).asParam)

  // Get an ASM classwriter, passing it the data needed to dynamically generate a class, "dump" the bytecode
  val bytecode = BytecodeGenerator.dump(classData)
  val runtimeClass: java.lang.Class[_] = DynamicClassLoader.loadClass(fullName, bytecode(0)) //load the class
  val companionClass: java.lang.Class[_] = DynamicClassLoader.loadClass(fullName + "$", bytecode(1)) //load the module class

  // Now that we've loaded our classes, use reflection to get an instance of the companion class
  val method_apply = companionClass.getMethod("apply", methodParams: _*) // populate the instance values
  val companionCtor = companionClass.getConstructor()
  val runtimeCompanionObject = companionCtor.newInstance() // get an instance of the companion
  val runtimeInstance: java.lang.Object = method_apply.invoke(runtimeCompanionObject, instantiationParams: _*)

  // Offer a method to return new instances of the runtime case class
  def newInstance(arguments: Any*) = {
  	val obs = arguments.map(x=>x.asInstanceOf[Object])
  	method_apply.invoke(runtimeCompanionObject, obs: _*)
  }
 
  // Get a  type tag for the new class (adapted from http://stackoverflow.com/questions/22970209/get-typetaga-from-classa/22972751#22972751
  import scala.reflect.runtime.universe._
  import scala.reflect.api._

  val mirror = runtimeMirror(runtimeClass.getClassLoader) // obtain runtime mirror
  val sym = mirror.staticClass(runtimeClass.getName) // obtain class symbol for `c`
  val tpe = sym.selfType // obtain type object for `c`

  // Create a type tag which contains a type object
  val tt = TypeTag(mirror, new TypeCreator {
    def apply[U <: Universe with Singleton](m: scala.reflect.api.Mirror[U]) =
      if (m eq mirror) tpe.asInstanceOf[U#Type]
      else throw new IllegalArgumentException(s"Type tag defined in $mirror cannot be migrated to other mirrors.")
  })

  // Use a dummy class to hold the type tag/manifest for the type member T - Thanks Eugene Burmako (http://stackoverflow.com/questions/25601058/can-i-use-a-class-defined-in-a-toolbox-as-a-type-parameter-of-typeof)
  class TypeCheckDummy {
    type T = AnyRef 
    implicit val tag: TypeTag[T] = tt.asInstanceOf[TypeTag[T]]
    implicit val manifest: Manifest[T] = Manifest.classType(runtimeClass)
  }

  // A type alias of the dummy class' type member T with TypeTag and Manifest, can be used as a type parameter
  val implicits = new TypeCheckDummy;
  import implicits._
  type TYPE = T  

  // Finally, after a DynamicCaseClass is made, add it to the list of generated Classes 
  ClassStore.accept(this)

}