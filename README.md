Case-Class-Generator
=====================

Allows runtime data to serve as Scala case class definitions:
* Case classes defined and loaded at runtime
* Pseudo Type-Provider via type alias

Scala Macros are still experimental, and so far runtime code-generation and evaluation can be accomplished in a `toolbox`, yet no only one class per package can be created. so for now I'm using ASM and [Artisinal-Pickle-Maker](https://github.com/julianpeeters/artisanal-pickle-maker) to generate bytecode for case classes, and a custom ClassLoader and Java reflection to instantiate the newly-made classes.

The dynamically generated class can be used to define a type alias, which itself can be used as a type parameter in some cases - see warnings below.

Supports generating classes with arbitrary fields of the following datatypes: 

* Byte
* Short
* Int
* Long
* Float
* Double
* Char
* String
* Boolean
* Null
* Nothing*
* Any
* AnyRef
* user-defined

*All datatypes except Nothing can be used as type-template/type-provider.


###Usage:

Add the following dependency: 
  

    "com.julianpeeters" %% "case-class-generator" % "0.4"

Then get a `DynamicCaseClass` instance with:


    import com.julianpeeters.caseclass.generator.{ FieldData, ClassData, DynamicCaseClass }

    val fieldData = List(FieldData("age", "Int"))
    val classData = ClassData(Some("mypackage"), "MyRuntimeClassName", fields)
    
    val dcc = new DynamicCaseClass(classData)

with which you will be a able to:


    // get an instance of the newly generated class
    dcc.runtimeInstance

    // get an instance of the newly generated class's companion object
    dcc.runtimeCompanionObject

    // get an alias for the runtime type for use as a type parameter (in some contexts, ymmv)
    myParameterizedThing[dcc.TYPE]


##Warnings: 
0) This is designed to dynamically provide a type parameter for the ScalaSig parsing libraries Salat and Salat-Avro. Further utilitiy is incidental. 

1) Enter Scala reflection as a `Symbol` if you are using a libary that uses Scala reflection. `typeOf[dcc.TYPE]` won't work. A `TypeTag` is normally generated at compile-time, but none are made for classes generated at runtime (anybody know how to spoof a `TypeTag`?). Instead use: 


    import scala.reflect.runtime._
    val rootMirror = universe.runtimeMirror(getClass.getClassLoader)
    var runtimeClassSymbol = rootMirror.classSymbol(dcc.runtimeInstance.getClass)


2) Only the generation of case classes with fields but no body are supported. This is due to concerns about hygeine, specifically restricting the generation of anonymous classes that may pollute the namespace. For now only classes with vals can be generated: no vars. It is feasible to add them in the future, but for them to work, artisanal-pickle-maker must be updated as well.

3) Neither objectWeb's classwriter nor my artisanal-pickle-maker are thread-safe.

4) Reflection circumvents type-saftey. If you find yourself here, please consider if you truly need to define classes at runtime. For example, a file that is accessed at runtime is often *also* accessible at compile-time, and therefore is candidate for a macro, which is type-safe.

5) If you only need a single class per package, please see [toolbox-type-provider](https://github.com/julianpeeters/toolbox-type-provider) for the official, experimental way to do generate classes at runtime. Keep an eye on scala.meta to solve the single-class-per-package issue.

6) Criticism is appreciated!


