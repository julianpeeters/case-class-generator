package com.julianpeeters.caseclass.generator
import org.objectweb.asm._
import Opcodes._

object FieldMatcher {

  def enrichFieldData(namespace: Option[String], field: FieldData): TypedFields = {
    val fieldType = field.fieldType
    TypedFields(
      field.fieldName,
      fieldType,
      getTypeData(namespace, fieldType)
    )
  }

  def getBoxed(typeName: String) = {
    typeName.dropWhile(c => (c != '[')).drop(1).dropRight(1)
  }

  def getUnerasedTypeName(namespace: Option[String], typeName: String) = {
    typeName match {
      case "Null" | "Boolean" | "Int" | "Long" | "Float" | "Double" | "String" | "Byte" | "Short" | "Char" | "Any" | "AnyRef" | "Unit" | "Nothing" | "Object" => null
      case l: String if l.startsWith("List[") => {
        "Lscala/collection/immutable/List<" + getUnapplyType(namespace, getBoxed(l)) + ">;"
      }
      case o: String if o.startsWith("Option[") => "Lscala/Option<" + getUnapplyType(namespace, getBoxed(o)) + ">;"
      case u: String                            => null
    }
  }

  def getUnapplyType(namespace: Option[String], typeName: String): String = {
    typeName match {
      case "String" => "Ljava/lang/String;"
      case "Null" | "Boolean" | "Int" | "Long" | "Float" | "Double" | "Byte" | "Short" | "Char" | "Any" | "AnyRef" | "Unit" | "Nothing" | "Object" => "Ljava/lang/Object;"
      case l: String if l.startsWith("List[") => {
        "Lscala/collection/immutable/List<" + getUnapplyType(namespace, getBoxed(l)) + ">;"
      }
      case o: String if o.startsWith("Option[") => "Lscala/Option<" + getUnapplyType(namespace, getBoxed(o)) + ">;"
      //user-defined
      case u: String => {
        if (namespace.isDefined) "L" + namespace.get + "/" + typeName + ";"
        else "L" + typeName + ";"
      }
    }
  }

  def getUnerasedTypeDescriptor(namespace: Option[String], typeName: String): String = {
    typeName match {
      case "Null"    => Type.getDescriptor(classOf[Null])
      case "Boolean" => Type.getDescriptor(classOf[Boolean])
      case "Int"     => Type.getDescriptor(classOf[Int])
      case "Long"    => Type.getDescriptor(classOf[Long])
      case "Float"   => Type.getDescriptor(classOf[Float])
      case "Double"  => Type.getDescriptor(classOf[Double])
      case "String"  => Type.getDescriptor(classOf[String])
      case "Byte"    => Type.getDescriptor(classOf[Byte])
      case "Short"   => Type.getDescriptor(classOf[Short])
      case "Char"    => Type.getDescriptor(classOf[Char])
      case "Any"     => Type.getDescriptor(classOf[Any])
      case "AnyRef"  => Type.getDescriptor(classOf[AnyRef])
      case "Unit"    => Type.getDescriptor(classOf[Unit])
      case "Nothing" => Type.getDescriptor(classOf[Nothing])
      case "Object"  => Type.getDescriptor(classOf[Object])

      case l: String if l.startsWith("List[") => {
        "Lscala/collection/immutable/List<" + getUnapplyType(namespace, getBoxed(l)) + ">;"
      }
      case o: String if o.startsWith("Option[") => "Lscala/Option<" + getUnapplyType(namespace, getBoxed(o)) + ">;"
      case u: String => { //user defined
        if (namespace.isDefined) "L" + namespace.get + "/" + typeName + ";"
        else "L" + typeName + ";"
      }

    }
  }

  def getExampleObject(typeName: String): Object = {
    typeName match {
      case "Null"                               => null.asInstanceOf[Object]
      case "Boolean"                            => true.asInstanceOf[Object]
      case "Int"                                => 1.asInstanceOf[Object]
      case "Long"                               => 1L.asInstanceOf[Object]
      case "Float"                              => 1F.asInstanceOf[Object]
      case "Double"                             => 1D.asInstanceOf[Object]
      case "String"                             => ""
      case "Byte"                               => 1.toByte.asInstanceOf[Object]
      case "Short"                              => 1.toShort.asInstanceOf[Object]
      case "Char"                               => 'k'.asInstanceOf[Object]
      case "Any"                                => "".asInstanceOf[Any].asInstanceOf[Object]
      case "AnyRef"                             => "".asInstanceOf[AnyRef].asInstanceOf[Object]
      case "Unit"                               => ().asInstanceOf[scala.runtime.BoxedUnit]
      case "Nothing"                            => null
      case "Object"                             => new Object

      case l: String if l.startsWith("List[")   => List(getExampleObject(getBoxed(l))).asInstanceOf[List[Any]].asInstanceOf[Object]
      case o: String if o.startsWith("Option[") => Option(getExampleObject(getBoxed(o))).asInstanceOf[Option[Any]].asInstanceOf[Object]
      case u: String                            => ClassStore.generatedClasses.get(typeName).get.runtimeInstance
    }
  }

  /*
From the ASM userguide:
The ILOAD, LLOAD, FLOAD, DLOAD, and ALOAD instructions read a local variable
and push its value on the operand stack. They take as argument the index
i of the local variable that must be read. ILOAD is used to load a boolean,
byte, char, short, or int local variable. LLOAD, FLOAD and DLOAD are used to
load a long, float or double value, respectively (LLOAD and DLOAD actually
load the two slots i and i+ 1). Finally ALOAD is used to load any non primitive
value, i.e. Object and array references.
*/

  def getTypeData(namespace: Option[String], fieldType: String): TypeData = {
    fieldType match {
      case "Null" => {
        TypeData(
          Type.getDescriptor(classOf[Null]),
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Boolean" => {
        TypeData(
          Type.getDescriptor(classOf[Boolean]),
          getUnapplyType(namespace, fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Int" => {
        TypeData(
          Type.getDescriptor(classOf[Int]),
          getUnapplyType(namespace, fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Long" => {
        TypeData(Type.getDescriptor(classOf[Long]),
          getUnapplyType(namespace, fieldType),
          LLOAD,
          LRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Float" => {
        TypeData(
          Type.getDescriptor(classOf[Float]),
          getUnapplyType(namespace, fieldType),
          FLOAD,
          FRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Double" => {
        TypeData(
          Type.getDescriptor(classOf[Double]),
          getUnapplyType(namespace, fieldType),
          DLOAD,
          DRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "String" => {
        TypeData(
          Type.getDescriptor(classOf[String]),
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Byte" => {
        TypeData(
          Type.getDescriptor(classOf[Byte]),
          getUnapplyType(namespace, fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Short" => {
        TypeData(
          Type.getDescriptor(classOf[Short]),
          getUnapplyType(namespace, fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Char" => {
        TypeData(
          Type.getDescriptor(classOf[Char]),
          getUnapplyType(namespace, fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Any" => {
        TypeData(
          Type.getDescriptor(classOf[Any]),
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "AnyRef" => {
        TypeData(
          Type.getDescriptor(classOf[AnyRef]),
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Unit" => {
        TypeData(
          "Lscala/runtime/BoxedUnit;", //Type.getDescriptor(classOf[Unit])
          getUnapplyType(namespace, fieldType),
          ALOAD,
          RETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Nothing" => {
        TypeData(
          Type.getDescriptor(classOf[Nothing]),
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case "Object" => {
        TypeData(
          Type.getDescriptor(classOf[Object]),
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }

      //Complex 

      case name: String if name.startsWith("List[") => {
        TypeData(
          Type.getDescriptor(classOf[List[Any]]),
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }
      case name: String if name.startsWith("Option[") => {
        TypeData(
          Type.getDescriptor(classOf[Option[Any]]),
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }

      //User-Defined
      case name: String => {
        TypeData(
          if (namespace.isDefined) "L" + namespace.get + "/" + name + ";"
          else "L" + name + ";", //if its a string but none of the above, its a nested record type
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(name),
          getUnerasedTypeName(namespace, name),
          getUnerasedTypeDescriptor(namespace, name)
        )
      }
      case _ => error("only Strings are valid type names")
    }
  }

  def getReturnType(fieldSeeds: List[FieldData]) = {
    fieldSeeds.map(n => n.fieldType).map(m => m match {
      //    case "Null"    => classOf[Unit]
      case "Boolean"                            => classOf[Boolean]
      case "Int"                                => classOf[Int]
      case "Long"                               => classOf[Long]
      case "Float"                              => classOf[Float]
      case "Double"                             => classOf[Double]
      case "bytes"                              => classOf[Seq[Byte]]
      case "String"                             => classOf[String]
      case "Short"                              => classOf[Short]
      case "Byte"                               => classOf[Byte]
      case "Char"                               => classOf[Char]
      case "Any"                                => classOf[Any]
      case "AnyRef"                             => classOf[AnyRef]
      case "Unit"                               => classOf[scala.runtime.BoxedUnit] //classOf[Unit]
      case "Nothing"                            => classOf[Nothing]
      case "Null"                               => classOf[Null]
      case "Object"                             => classOf[Object]
      //Complex ------------------------

      case "enum"                               => classOf[Enumeration#Value]
      case "array"                              => classOf[Seq[_]]
      case "map"                                => classOf[Map[String, _]]

      case l: String if l.startsWith("List[")   => classOf[List[Any]]
      case o: String if o.startsWith("Option[") => classOf[Option[Any]]
      case x: String                            => ClassStore.generatedClasses.get(x).get.runtimeClass
    })
  }
}
