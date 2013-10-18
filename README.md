ASM-type-provider
=================

Allows runtime data to serve as Scala case class definitions. 

While Scala Macro Annotations are under development (and for projects stuck on scala 2.9), This project uses ASM and Artisinal-Pickle-Maker to generate bytecode for case classes, and a custom ClassLoader and Java reflection to instantiate the newly-made classes (can't use Scala's typeOf[], at least until we've got a type in a later step!). 

The dynamically generated class can be partially* typed by using an instance of the generated class as a template to define a type alias. This allows the class to be used as a type parameter, and with it's parseable pickled Scala signature, as a model for serialization by Salat or Salat-Avro.

*By *partially* typed I mean that the type can be used as a type parameter in some situations, e.g. typeOf[], but not  classOf[] (where a class type is required, for example, or when a .class file is required).


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
Any



##Warnings: 
0) Designed to dynamically provide a type parameter for Salat and Salat-Avro. Access to the dynamic classes seems to be limited to Java reflection and parsing the Scala sig directly, i.e. access via Scala 2.10 reflection is not yet supported.

1) Only modestly tested on classes with one field only.

2) Neither objectWeb's classwriter nor my artisinal-pickle-maker are thread-safe.

3) I'm new at this so, please, criticism is appreciated!



