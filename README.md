Case-Class-Generator
=================

Allows runtime data to serve as Scala case class definitions:
* Case classes defined and loaded at runtime
* Accessed via Java reflection
* Pseudo Type-Provider via type alias

Scala Macros are still under development, so for now I'm using ASM and Artisinal-Pickle-Maker to generate bytecode for case classes, and a custom ClassLoader and Java reflection to instantiate the newly-made classes (can't use Scala's typeOf[], at least until we've got a type in a later step!). 

The dynamically generated class can be partially typed by using an instance of the generated class as a template to define a type alias. "Partially" because the type alias can be used as a type parameter in some cases - see warnings below.


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

*All datatypes except Nothing can be used as type-template/type-provider.

 By commenting out the appropriate methods, and with a little fiddling with the "case class" flag, regular classes can be made too. 


##Warnings: 
1)  This is designed to dynamically provide a type parameter for Salat and Salat-Avro. Further utilitiy is incidental. Access to the dynamic classes seems to be limited to Java reflection and parsing the Scala sig directly, i.e. access via Scala 2.10 reflection is not yet supported. Further research is required to determine boundaries of its functionality as a type.  

2)  For now only classes with vals can be generated: no vars, no defs. It is feasible to add them in the future, but for them to work, artisinal-pickle-maker must be updated as well.

3)  Neither objectWeb's classwriter nor my artisinal-pickle-maker are thread-safe.

4)  I'm new at this so, please, criticism is appreciated!



