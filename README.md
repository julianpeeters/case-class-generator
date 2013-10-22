Case-Class-Generator
=================

Allows runtime data to serve as Scala case class definitions:
-Defined and loaded at runtime
-Access via Java reflection
-Pseudo Type-Provider

Scala Macros are still under development, so for now use ASM and Artisinal-Pickle-Maker to generate bytecode for case classes, and a custom ClassLoader and Java reflection to instantiate the newly-made classes (can't use Scala's typeOf[], at least until we've got a type in a later step!). 

The dynamically generated class can be partially typed by using an instance of the generated class as a template to define a type alias. "Partially" because the type alias can be used as a type parameter in some cases - see warnings below.


Supports generating classes with arbitrary fields of the following datatypes: 

Byte
Short
Int
Long
Float
Double
Char
String
Boolean
Null
Nothing*
Any
AnyRef

*All datatypes except Nothing can be used as type-template/type-provider.

##Warnings: 
1) Designed to dynamically provide a type parameter for Salat and Salat-Avro. Access to the dynamic classes seems to be limited to Java reflection and parsing the Scala sig directly, i.e. access via Scala 2.10 reflection is not yet supported.  
As a type alias, the class is only "partially typed". It can be used as a type parameter but it is still just a type alias. E.g., it can be used in `TypeOf[]`, but it is not a class type and so `ClassOf[]` gives an error, and will fail if a .class file is required. Further research is required to determine boundaries of its functionality as a type.  

2) Neither objectWeb's classwriter nor my artisinal-pickle-maker are thread-safe.

3) I'm new at this so, please, criticism is appreciated!



