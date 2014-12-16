Case-Class-Generator
=====================

Allows runtime data to serve as Scala case class definitions:
* Case classes defined and loaded at runtime
* Pseudo Type-Provider via type alias

Runtime code-generation and evaluation can be accomplished in a `scala.tools.reflect.ToolBox`, yet only one class per package can be created (see [this error](https://github.com/julianpeeters/toolbox-salat-example/blob/two_classes_error/src/main/scala/Main.scala#L59)). So for now I'm using [ASM](http://asm.ow2.org/) and [Artisinal-Pickle-Maker](https://github.com/julianpeeters/artisanal-pickle-maker) to generate bytecode for case classes, and a custom classloader to load the newly-made classes.

The dynamically generated class can be used to instantiate new objects at runtime, or serve as as a type parameter. Please see warnings below.

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
* List
* Option
* user-defined (i.e. other case classes)

*All datatypes except Nothing can be used as type-provider.


###Usage:

Add the following dependency: [![Build Status](https://travis-ci.org/julianpeeters/case-class-generator.svg?branch=scala_2.10)](https://travis-ci.org/julianpeeters/case-class-generator)
  

    "com.julianpeeters" %% "case-class-generator" % "0.6.1"

Then get a `DynamicCaseClass` instance with:


    import com.julianpeeters.caseclass.generator._

    val name      = ClassName("Person")
    val namespace = ClassNamespace(Some("mypackage"))
    val fieldData = ClassFieldData( List(FieldData("name", typeOf[String]), FieldData("age", typeOf[Int])) )
    val classData = ClassData(namespace, name, fieldData)
    
    val dcc = new DynamicCaseClass(classData)


with which you will be a able to:


* Use the `newInstance(varargs: Any*)` method to get an instance of the newly-generated class:
    `val record = dcc.newInstance("Griselda", 4)`


* Use a "zero-arg constructor" to get and instance of the newly-generated class with default parameters:
    `val record = dcc.runtimeInstance`


* Call `.type` on an instance of the runtime class to use it as a type parameter (doesn't carry a type tag):
    `val x = myParamterizedThing[record.type]`


* Import the new  class' implicits and use the `.TYPE` type member as a type parameter (carries a type tag/manifest, implicit scoping rules apply):



    import scala.reflect.runtime.universe._
    import dcc.implicits.{ tag, manifest }

    typeOf[dcc.TYPE]


* Retrieve previously generated classes from a `Map[Type, DynamicCaseClass]`
     `ClassStore.generatedClasses.get(dcc.tpe)`


##Warnings: 

1) Reflection circumvents type-saftey. If you find yourself here, please consider if you truly need to define classes at runtime. For example, a file that is accessed at runtime is often *also* accessible at compile-time, and therefore is candidate for a macro, which is type-safe.

2) If you only need a single class per package, please see [toolbox-type-provider](https://github.com/julianpeeters/toolbox-type-provider) for a tool that uses the official (yet still experimental) way to generate classes at runtime. Keep an eye on [scala.meta])(http://scalameta.org/) to solve the single-class-per-package issue.

3) Only the generation of case classes with fields but without a body are supported. This is due to concerns about hygeine, specifically restricting the generation of anonymous classes that may pollute the namespace. 

4) For now only classes with vals can be generated: no vars. It is feasible to add vars in the future, but for them to work, [Artisinal-Pickle-Maker](https://github.com/julianpeeters/artisanal-pickle-maker) must be updated as well.

5) ObjectWeb's ASM classwriter, and thus this project, is _not_ thread-safe. Testing with Travis CI is done by using `parallelExecution in Test := false` in the build file.


Fork away, just make sure the tests pass. Criticism is appreciated.


