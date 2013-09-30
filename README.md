ASM-type-provider
=================

Dynamically defines and instantiates Scala case classes at runtime. This allows for certain JSON, such as Avro schemas, to serve as case class definitions.

While Scala Macro Annotations are under development, I'll use ASM and Artisinal-Pickle-Maker to generate bytecode for case classes, and a custom ClassLoader and Java reflection to instantiate it (can't use Scala's typeOf[] until we've got a type in a later step!). 

The dynamically generated class can be *partially* typed by using an instance to define a type alias. This allows the class to be used as a *type parameter, and with it's parseable pickled Scala signature, as a model for serialization by Salat or Salat-Avro.

Supports generating classes with arbitrary fields of the following datatypes: String, Int, Boolean  // More to come

*By *partially* typed I mean that the type can be used as a type parameter in some situations, e.g. typeOf[], but not  classOf[] (where a class type is required, for example, or when a .class file is required).


Warnings: 

1) There is currently a `ByteCodecs.encode`/pickler issue that is limiting the classes to only a couple fields per class.

2) Neither objectWeb's classwriter nor my artisinal-pickle-maker are thread-safe.

3) I'm new at this so please, criticism is appreciated!



