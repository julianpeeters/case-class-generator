package caseclass.generator
import org.objectweb.asm._
import Opcodes._



object FieldMatcher {
println("FM")

  def enrichFieldData(namespace: String, field: FieldSeed): FieldData = {

    val fieldType = field.fieldType

    FieldData( 
      field.fieldName, 
      fieldType,
      getTypeData(namespace, fieldType) 
    )
  }

  def getBoxed(typeName: String) = {
    typeName.dropWhile( c => (c != '[') ).drop(1).dropRight(1)
  }


  
  def getUnerasedTypeName(typeName: String): String = {
    typeName match {
      case "Null"|"Boolean"| "Int"| "Long"|"Float"| "Double"| "String"| "Byte"| "Short"| "Char"| "Any"| "AnyRef"| "Unit"|"Nothing"|"Object" => "null"
      case l: String if l.startsWith("List[")   => "Lscala/collection/immutable/List<" + getUnapplyType(getBoxed(l)) + ">;"
      case o: String if o.startsWith("Option[") => ""
      case u: String =>
    }
  }

  def getUnapplyType(typeName: String): String = {
    typeName match {
      case  "String" => "Ljava/lang/String;"
      case "Null"|"Boolean"|"Int"|"Long"|"Float"|"Double"|"Byte"|"Short"|"Char"|"Any"|"AnyRef"|"Unit"|"Nothing"|"Object" => "Ljava/lang/Object;"
      case l: String if l.startsWith("List[")   => "Lscala/collection/immutable/List<" + getUnapplyType(getBoxed(l)) + ">;"
      case o: String if o.startsWith("Option[") => ""
      //user-defined
      case u: String => "L" + namespace + "/" + typeName + ";"
    }
  }

  def getUnerasedTypeDescriptor(typeName: String): String = {
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

      case l: String if l.startsWith("List[")   => "Lscala/collection/immutable/List<" + getUnapplyType(getBoxed(l)) + ">;"
      case o: String if o.startsWith("Option[") => ""
    }
  }

  def getExampleObject(typeName: String): Object = {
    typeName match {
      case "Null"    => null.asInstanceOf[Object]
      case "Boolean" => true.asInstanceOf[Object]
      case "Int"     => 1.asInstanceOf[Object]
      case "Long"    => 1L.asInstanceOf[Object]
      case "Float"   => 1F.asInstanceOf[Object]
      case "Double"  => 1D.asInstanceOf[Object]
      case "String"  => ""
      case "Byte"    => 1.toByte.asInstanceOf[Object]
      case "Short"   => 1.toShort.asInstanceOf[Object]
      case "Char"    => 'k'.asInstanceOf[Object]
      case "Any"     => "".asInstanceOf[Any].asInstanceOf[Object]
      case "AnyRef"  => "".asInstanceOf[AnyRef].asInstanceOf[Object]
      case "Unit"    => ().asInstanceOf[scala.runtime.BoxedUnit]
      case "Nothing" => null
      case "Object"  => new Object

      case l: String if l.startsWith("List[") => List(getExampleObject(getBoxed(l))).asInstanceOf[List[Any]].asInstanceOf[Object]
      case o: String if o.startsWith("Option[") => Option(getExampleObject(getBoxed(o))).asInstanceOf[Option[Any]].asInstanceOf[Object]
    }
  }

  def getTypeData(namespace: String, fieldType: String): TypeData = {
    fieldType match {
      case "Null"    => { 
        TypeData(
          Type.getDescriptor(classOf[Null]),
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType), 
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Boolean" => {
        TypeData(
          Type.getDescriptor(classOf[Boolean]),
          getUnapplyType(fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Int"     => {
        TypeData(
          Type.getDescriptor(classOf[Int]),
          getUnapplyType(fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Long"    => {
        TypeData(Type.getDescriptor(classOf[Long]),
          getUnapplyType(fieldType),
          LLOAD,
          LRETURN, 
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Float"   => {
        TypeData(
          Type.getDescriptor(classOf[Float]),
          getUnapplyType(fieldType),
          FLOAD,
          FRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Double"  => {
        TypeData(
          Type.getDescriptor(classOf[Double]),
          getUnapplyType(fieldType),
          DLOAD,
          DRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "String"  => {
        TypeData(
          Type.getDescriptor(classOf[String]),
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Byte"    => {
        TypeData(
          Type.getDescriptor(classOf[Byte]),
          getUnapplyType(fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Short"    => {
        TypeData(
          Type.getDescriptor(classOf[Short]),
          getUnapplyType(fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Char"    => {
        TypeData(
          Type.getDescriptor(classOf[Char]),
          getUnapplyType(fieldType),
          ILOAD,
          IRETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Any"    => {
        TypeData(
          Type.getDescriptor(classOf[Any]),
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "AnyRef"    => {
        TypeData(
          Type.getDescriptor(classOf[AnyRef]),
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Unit"    => {
        TypeData(
          "Lscala/runtime/BoxedUnit;",//Type.getDescriptor(classOf[Unit])
          getUnapplyType(fieldType),
          ALOAD,
          RETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Nothing"    => {
        TypeData(
          Type.getDescriptor(classOf[Nothing]),
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case "Object"    => {
        TypeData(
          Type.getDescriptor(classOf[Object]),
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }

      //Complex 
/*
      case name: String if name.startsWith("List[") => { 
        TypeData(
          Type.getDescriptor(classOf[List[Any]]),
          "Ljava/lang/Object;",
          ALOAD,
          ARETURN,
          List(List(1,2), List(3,4)).asInstanceOf[List[Any]].asInstanceOf[Object]
        )
      } 
*/
      case name: String if name.startsWith("List[") => { 
        TypeData(
          Type.getDescriptor(classOf[List[Any]]),
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
          //List(1,2).asInstanceOf[List[Any]].asInstanceOf[Object]
          getExampleObject(fieldType),
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      } 
      case name: String if name.startsWith("Option[") => { println("howdy")
        TypeData(
          Type.getDescriptor(classOf[Option[Any]]),
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
          Some("").asInstanceOf[Option[Any]].asInstanceOf[Object],
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      } 


      //User-Defined
      case name: String => { 
        TypeData(
//          "L"+ name + ";", //if its a string but none of the above, its a nested record type
          "L"+ namespace + "/" + name + ";", //if its a string but none of the above, its a nested record type
          getUnapplyType(fieldType),
          ALOAD,
          ARETURN,
//          CaseClassGenerator.generatedClasses.get(namespace + "." + name).get.instantiated$.asInstanceOf[Object]
          CaseClassGenerator.generatedClasses.get(name).get.instantiated$,
//          CaseClassGenerator.generatedClasses.get(name).get.instantiated$.asInstanceOf[Object]
          getUnerasedTypeName(fieldType),
          getUnerasedTypeDescriptor(fieldType)
        )
      }
      case _         => error("only Strings are valid type names")
    }
  }

/*
The ILOAD, LLOAD, FLOAD, DLOAD, and ALOAD instructions read a local variable
and push its value on the operand stack. They take as argument the index
i of the local variable that must be read. ILOAD is used to load a boolean,
byte, char, short, or int local variable. LLOAD, FLOAD and DLOAD are used to
load a long, float or double value, respectively (LLOAD and DLOAD actually
load the two slots i and i+ 1). Finally ALOAD is used to load any non primitive
value, i.e. Object and array references.
*/

  def getInstantiationTypes(schema: Any) = {      println("getting instantiation type")
    val ft = JSONParser.getFields(schema).map(n => n.fieldType)
    ft.map(m => m match {
      //Primitive Avro types --- Thanks to @ConnorDoyle for suggesting the type mapping
      //    case "Null"    => classOf[Unit]
      case "Boolean" => classOf[Boolean]
      case "Int"     => classOf[Int]
      case "Long"    => classOf[Long]
      case "Float"   => classOf[Float]
      case "Double"  => classOf[Double]
     // case "bytes"   => classOf[Seq[Byte]]
      case "String"  => classOf[String]

      case "boolean" => classOf[Boolean]
      case "int"     => classOf[Int]
      case "long"    => classOf[Long]
      case "float"   => classOf[Float]
      case "double"  => classOf[Double]
      case "bytes"   => classOf[Seq[Byte]]
      case "string"  => classOf[String]

      //Complex ------------------------ 
      //case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
      case "enum"    => classOf[Enumeration#Value]
      case "array"   => classOf[Seq[_]]
      case "map"     => classOf[Map[String, _]]
   //   case "Map(type -> record, name -> rec, doc -> , fields -> List(Map(name -> i, type -> List(int, null))))"     => classOf[Map[String, _]]
      // case "union"   => classOf[]
      // case "[null,"+_+"]"      => 
      // case "[null,String]"      => classOf[Option[String]] 
      //case "fixed"   => classOf[]


      //  case "option"   =>  classOf[Option[Any]]
      case l: String if l.startsWith("List[") => classOf[List[Any]]         
                         
      case x: String =>CaseClassGenerator.generatedClasses.get(x).get.model //if its a string but none of the above, its a nested record type
      case _     => error("File parser found nuthin' good")

    })
  }


//def getReturnTypes(fieldSeeds: List[FieldSeed]) = {
def getReturnType(fieldSeeds: List[FieldSeed]) = { println("getting return types")
fieldSeeds.map(n => n.fieldType).map(m => m match { 
      //    case "Null"    => classOf[Unit]
      case "Boolean" => classOf[Boolean]
      case "Int"     => classOf[Int]
      case "Long"    => classOf[Long]
      case "Float"   => classOf[Float]
      case "Double"  => classOf[Double]
      case "bytes"   => classOf[Seq[Byte]]
      case "String"  => classOf[String]
      //Complex ------------------------
      //case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
      case "enum"    => classOf[Enumeration#Value]
      case "array"   => classOf[Seq[_]]
      case "map"     => classOf[Map[String, _]]
     // case "Map(type -> record, name -> rec, doc -> , fields -> List(Map(name -> i, type -> List(int, null))))"     => classOf[Map[String, _]]
   

                         case "Short"   => classOf[Short]
                         case "Byte"    => classOf[Byte]
                         case "Char"    => classOf[Char]
                         case "Any"     => classOf[Any]
                         case "AnyRef"  => classOf[AnyRef]
                         case "Unit"    => classOf[scala.runtime.BoxedUnit]//classOf[Unit]
                         case "Nothing" => classOf[Nothing]
                         case "Null"    => classOf[Null]
                         case "Object"  => classOf[Object]

      case l: String if l.startsWith("List[") => classOf[List[Any]]         
      case o: String if o.startsWith("Option[") => classOf[Option[Any]]         
      //case x: String => classOf[Class]                          
      case x: String => CaseClassGenerator.generatedClasses.get(x).get.model//Class.forName(x) //if its a string but none of the above, its a nested record type
    //  case a:Any     => a

    })
}


}
