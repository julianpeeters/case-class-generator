package com.julianpeeters.caseclass.generator

import org.objectweb.asm._
import Opcodes._
import scala.reflect.runtime.universe._

object FieldMatcher {

  def enrichFieldData(namespace: Option[String], field: FieldData): EnrichedField = {
    EnrichedField(
      field.fieldName,
      field.fieldType,
      getTypeData(namespace, field.fieldType)
    )
  }

  def getExampleObject(typeName: scala.reflect.runtime.universe.Type): Object = { 
    typeName match {
      case l if l =:= typeOf[Null]                               => null.asInstanceOf[Object]
      case l if l =:= typeOf[Boolean]                            => true.asInstanceOf[Object]
      case l if l =:= typeOf[Int]                                => 1.asInstanceOf[Object]
      case l if l =:= typeOf[Long]                               => 1L.asInstanceOf[Object]
      case l if l =:= typeOf[Float]                              => 1F.asInstanceOf[Object]
      case l if l =:= typeOf[Double]                             => 1D.asInstanceOf[Object]
      case l if l =:= typeOf[String]                             => ""
      case l if l =:= typeOf[Byte]                               => 1.toByte.asInstanceOf[Object]
      case l if l =:= typeOf[Short]                              => 1.toShort.asInstanceOf[Object]
      case l if l =:= typeOf[Char]                               => 'k'.asInstanceOf[Object]
      case l if l =:= typeOf[Any]                                => "".asInstanceOf[Any].asInstanceOf[Object]
      case l if l =:= typeOf[AnyRef]                             => "".asInstanceOf[AnyRef].asInstanceOf[Object]
      case l if l =:= typeOf[Unit]                               => ().asInstanceOf[scala.runtime.BoxedUnit]
      case l if l =:= typeOf[Nothing]                            => null
      case l if l =:= typeOf[Object]                             => new Object
      case la @ TypeRef(pre, symbol, args) if (la <:< typeOf[List[Any]] && args.length == 1) =>
        List(getExampleObject(args.head)).asInstanceOf[List[Any]].asInstanceOf[Object]
      case o @ TypeRef(pre, symbol, args) if (o <:< typeOf[Option[Any]] && args.length == 1) => 
        Option(getExampleObject(args.head)).asInstanceOf[Option[Any]].asInstanceOf[Object]
      case u @ TypeRef(pre, symbol, args) => ClassStore.generatedClasses.get(u).get.runtimeInstance 
    }
  }


  def getReturnType(fieldSeeds: List[FieldData]) = {
    fieldSeeds.map(n => n.fieldType).map(m => m match {

      case l if l =:= typeOf[Boolean]                            => classOf[Boolean]
      case l if l =:= typeOf[Int]                                => classOf[Int]
      case l if l <:< typeOf[Long]                               => classOf[Long]
      case l if l =:= typeOf[Float]                              => classOf[Float]
      case l if l =:= typeOf[Double]                             => classOf[Double]
      case l if l =:= typeOf[String]                             => classOf[String]
      case l if l =:= typeOf[Short]                              => classOf[Short]
      case l if l =:= typeOf[Byte]                               => classOf[Byte]
      case l if l =:= typeOf[Char]                               => classOf[Char]
      case l if l =:= typeOf[Any]                                => classOf[Any]
      case l if l =:= typeOf[AnyRef]                             => classOf[AnyRef]
      case l if l =:= typeOf[Unit]                               => classOf[scala.runtime.BoxedUnit] //classOf[Unit]
      case l if l =:= typeOf[Nothing]                            => classOf[Nothing]
      case l if l =:= typeOf[Null]                               => classOf[Null]
      case l if l =:= typeOf[Object]                             => classOf[Object]
      //Complex ------------------------
/*
      case "enum"                               => classOf[Enumeration#Value]
      case "array"                              => classOf[Seq[_]]
      case "map"                                => classOf[Map[String, _]]
*/
      case la @ TypeRef(pre, symbol, args) if (la <:< typeOf[List[Any]] && args.length == 1) => classOf[List[Any]]
      case o @ TypeRef(pre, symbol, args) if (o <:< typeOf[Option[Any]] && args.length == 1) => classOf[Option[Any]]
      case x @ TypeRef(pre, symbol, args) => ClassStore.generatedClasses.get(x).get.runtimeClass
    })
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

  def getTypeData(namespace: Option[String], fieldType: scala.reflect.runtime.universe.Type): TypeData = {
    fieldType match {
      case l if l =:= typeOf[Null] => {
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
      case l if l =:= typeOf[Boolean] => {
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
      case l if l =:= typeOf[Int] => {
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

      case l if l =:= typeOf[Long] => {
        TypeData(Type.getDescriptor(classOf[Long]),
          getUnapplyType(namespace, fieldType),
          LLOAD,
          LRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(namespace, fieldType),
          getUnerasedTypeDescriptor(namespace, fieldType)
        )
      }

      case l if l =:= typeOf[Float] => {
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
      case l if l =:= typeOf[Double] => {
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

      case x if x =:= typeOf[String] => {
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

      case l if l =:= typeOf[Byte] => {
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
      case l if l =:= typeOf[Short] => {
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
      case l if l =:= typeOf[Char] => {
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
      case l if l =:= typeOf[Any] => {
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
      case l if l =:= typeOf[AnyRef] => {
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
      case l if l =:= typeOf[Unit] => {
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
      case l if l =:= typeOf[Nothing] => {
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
      case l if l =:= typeOf[Object] => {
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
      case la if la <:< typeOf[List[Any]] => { 
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
      case la if la <:< typeOf[Option[Any]] => {
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
      case name @ TypeRef(pre, symbol, args) => {
        TypeData(
          "L" + name.toString.replaceAllLiterally(".", "/") + ";", 
          getUnapplyType(namespace, fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(name),
          getUnerasedTypeName(namespace, name),
          getUnerasedTypeDescriptor(namespace, name)
        )
      }
      case _ => error("couldn't match field type")
    }
  }

  def getUnapplyType(namespace: Option[String], typeName: scala.reflect.runtime.universe.Type): String = { 
    typeName match {
      case  s if s =:= typeOf[String]  => "Ljava/lang/String;"
      case l if (l =:= typeOf[Null] | 
        l =:= typeOf[Boolean] | 
        l =:= typeOf[Int] | 
        l =:= typeOf[Long] | 
        l =:= typeOf[Float] | 
        l =:= typeOf[Double] | 
        l =:= typeOf[Byte] | 
        l =:= typeOf[Short] | 
        l =:= typeOf[Char] | 
        l =:= typeOf[Any] | 
        l =:= typeOf[AnyRef] | 
        l =:= typeOf[Unit] | 
        l =:= typeOf[Nothing] | 
        l =:= typeOf[Object] ) => "Ljava/lang/Object;"
      //generics
      case la @ TypeRef(pre, symbol, args) if (la <:< typeOf[List[Any]] && args.length == 1) => { 
        "Lscala/collection/immutable/List<" + getUnapplyType(namespace, args.head) + ">;"
      }
      case o @ TypeRef(pre, symbol, args) if (o <:< typeOf[Option[Any]] && args.length == 1) => { 
        "Lscala/Option<" + getUnapplyType(namespace, args.head) + ">;"
      }
      //user-defined or other classes
      case u @ TypeRef(pre, symbol, args) => {
        "L" + typeName.toString.replaceAllLiterally(".","/") + ";"
      }
    }
  }

  def getUnerasedTypeDescriptor(namespace: Option[String], typeName: scala.reflect.runtime.universe.Type): String = {
    typeName match {

      case l if l =:= typeOf[Null]    => Type.getDescriptor(classOf[Null])
      case l if l =:= typeOf[Boolean] => Type.getDescriptor(classOf[Boolean])
      case l if l =:= typeOf[Int]     => Type.getDescriptor(classOf[Int])
      case l if l =:= typeOf[Long]    => Type.getDescriptor(classOf[Long])
      case l if l =:= typeOf[Float]   => Type.getDescriptor(classOf[Float])
      case l if l =:= typeOf[Double]  => Type.getDescriptor(classOf[Double])
      case x if x =:= typeOf[String]  => Type.getDescriptor(classOf[String])
      case l if l =:= typeOf[Byte]    => Type.getDescriptor(classOf[Byte])
      case l if l =:= typeOf[Short]   => Type.getDescriptor(classOf[Short])
      case l if l =:= typeOf[Char]    => Type.getDescriptor(classOf[Char])
      case l if l =:= typeOf[Any]     => Type.getDescriptor(classOf[Any])
      case l if l =:= typeOf[AnyRef]  => Type.getDescriptor(classOf[AnyRef])
      case l if l =:= typeOf[Unit]    => Type.getDescriptor(classOf[Unit])
      case l if l =:= typeOf[Nothing] => Type.getDescriptor(classOf[Nothing])
      case l if l =:= typeOf[Object]  => Type.getDescriptor(classOf[Object])
      // generics
      case la @ TypeRef(pre, symbol, args) if (la <:< typeOf[List[Any]] && args.length == 1) => { 
        "Lscala/collection/immutable/List<" + getUnapplyType(namespace, args.head) + ">;"
      }
      case o @ TypeRef(pre, symbol, args) if (o <:< typeOf[Option[Any]] && args.length == 1) => { 
        "Lscala/Option<" + getUnapplyType(namespace, args.head) + ">;"
      }
      //user defined
      case TypeRef(pre, symbol, args) => { 
        "L" + typeName.toString.replaceAllLiterally(".", "/") + ";"
      }
      case _ => error("could not determine a descriptor corresponding to the unerased type ")
    }
  }

  def getUnerasedTypeName(namespace: Option[String], typeName: scala.reflect.runtime.universe.Type) = {
    typeName match {
      case l if (l =:= typeOf[Null] | 
        l =:= typeOf[Boolean] | 
        l =:= typeOf[Int] | 
        l =:= typeOf[Long] | 
        l =:= typeOf[Float] |
        l =:= typeOf[Double] | 
        l =:= typeOf[String] | 
        l =:= typeOf[Byte] | 
        l =:= typeOf[Short] |
        l =:= typeOf[Char] | 
        l =:= typeOf[Any] | 
        l =:= typeOf[AnyRef] | 
        l =:= typeOf[Unit] | 
        l =:= typeOf[Nothing] | 
        l =:= typeOf[Object] ) => null
      // complex types
      case la @ TypeRef(pre, symbol, args) if (la <:< typeOf[List[Any]] && args.length == 1) => { 
        "Lscala/collection/immutable/List<" + getUnapplyType(namespace, args.head) + ">;"
      }
      case o @ TypeRef(pre, symbol, args) if (o <:< typeOf[Option[Any]] && args.length == 1) => { 
        "Lscala/Option<" + getUnapplyType(namespace, args.head) + ">;"
      }
      //user defined or other case classes
      case u @ TypeRef(pre, symbol, args) => null
    }
  }

}
