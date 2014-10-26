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
* user-defined (i.e. other case classes)

*All datatypes except Nothing can be used as type-provider.


###Usage:

Add the following dependency: 
  

    "com.julianpeeters" %% "case-class-generator" % "0.5"

Then get a `DynamicCaseClass` instance with:


    import com.julianpeeters.caseclass.generator.{ FieldData, ClassData, DynamicCaseClass }

    val fieldData = List(FieldData("name", "String"), FieldData("age", "Int")
    val classData = ClassData(Some("mypackage"), "Person", fieldData)
    
    val dcc = new DynamicCaseClass(classData)

with which you will be a able to:


* Use the `newInstance(varargs: Any*)` method to get an instance of the newly-generated class:
    val record = dcc.newInstance("Dancing Queen", 17)


* Import the newly-generated class' type tag/manifest, and use the `TYPE` type member as a type parameter
    import dcc.implicits._
    myParameterizedThing[dcc.TYPE]


##Warnings: 
1) Only the generation of case classes with fields but without a body are supported. This is due to concerns about hygeine, specifically restricting the generation of anonymous classes that may pollute the namespace. 

2) For now only classes with vals can be generated: no vars. It is feasible to add vars in the future, but for them to work, [Artisinal-Pickle-Maker](https://github.com/julianpeeters/artisanal-pickle-maker) must be updated as well.

3) Neither objectWeb's ASM classwriter nor [Artisinal-Pickle-Maker](https://github.com/julianpeeters/artisanal-pickle-maker) are thread-safe.

4) Reflection circumvents type-saftey. If you find yourself here, please consider if you truly need to define classes at runtime. For example, a file that is accessed at runtime is often *also* accessible at compile-time, and therefore is candidate for a macro, which is type-safe.

5) If you only need a single class per package, please see [toolbox-type-provider](https://github.com/julianpeeters/toolbox-type-provider) for a tool that uses the official (yet still experimental) way to generate classes at runtime. Keep an eye on [scala.meta])(http://scalameta.org/) to solve the single-class-per-package issue.

6) Criticism is appreciated!


