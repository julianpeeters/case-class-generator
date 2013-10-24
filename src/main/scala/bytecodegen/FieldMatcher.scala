package caseclass.generator
import org.objectweb.asm._
import Opcodes._

object FieldMatcher {

  def enrichFieldData(field: FieldSeed): FieldData = {
    val fieldType = field.fieldType
    FieldData(field.fieldName, fieldType,
        toTypeDescriptor(fieldType), 
        getUnapplyType(fieldType),
        getLoadInstr(fieldType), 
        getReturnInstr(fieldType),
        getObject(fieldType) 
    )
  }


 def toTypeDescriptor(fieldType: String) = {(
    fieldType match {
      //Primitive Avro types --- Thanks to @ConnorDoyle for suggesting the type mapping
                         case "Null"    => Type.getDescriptor(classOf[Null])
                         case "Boolean" => Type.getDescriptor(classOf[Boolean])
                         case "Int"     => Type.getDescriptor(classOf[Int])
                         case "Long"    => Type.getDescriptor(classOf[Long])
                         case "Float"   => Type.getDescriptor(classOf[Float])
                         case "Double"  => Type.getDescriptor(classOf[Double])

                         case "bytes"   => Type.getDescriptor(classOf[Seq[Byte]])
                         case "String"  => Type.getDescriptor(classOf[String])
                         //Complex ------------------------ Not Supported in Salat-Avro?
  //                       case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
                         case "enum"    => Type.getDescriptor(classOf[Enumeration#Value])
                         case "array"   => Type.getDescriptor(classOf[Seq[_]])
                         case "map"     => Type.getDescriptor(classOf[Map[String, _]])
                         case "Map(type -> record, name -> rec, doc -> , fields -> List(Map(name -> i, type -> List(int, null))))"     => Type.getDescriptor(classOf[Map[String, _]])
                      // case "union"   => classOf[]
                      // case "[null,"+_+"]"      => 
                      // case "[null,String]"      => classOf[Option[String]] 
                       //case "fixed"   => classOf[]

                    //other Scala datatypes
                         case "Byte"    => Type.getDescriptor(classOf[Byte])
                         case "Short"    => Type.getDescriptor(classOf[Short])
                         case "Char"    => Type.getDescriptor(classOf[Char])
                         case "Any"    => Type.getDescriptor(classOf[Any])
                         case "AnyRef"    => Type.getDescriptor(classOf[AnyRef])
                         case "Unit"    => "Lscala/runtime/BoxedUnit;"//Type.getDescriptor(classOf[Unit])
                         case "Nothing"    => Type.getDescriptor(classOf[Nothing])

                         case "Object"    => Type.getDescriptor(classOf[Object])
                       // case "option"   =>  Type.getDescriptor(classOf[Option[Any]])           
                        ///   case n: List[Any] => classOf[Option[Any]]         
                         
                         case x: String => "L"+ x + ";" //if its a string but none of the above, its a nested record type

                         case _         => "Avro Schemas should only contain Primitive and Complex Avro types"
                        }).toString
  }

  def getUnapplyType(fieldType: String): String = {
    fieldType match {
                         //Primitive Avro types --- Thanks @ConnorDoyle for the type mapping
                   //    case "Null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "Boolean" => "Ljava/lang/Object;"
                         case "Int"     => "Ljava/lang/Object;"
                         case "Long"    => "Ljava/lang/Object;"
                         case "Float"   => "Ljava/lang/Object;"
                         case "Double"  => "Ljava/lang/Object;"
                       //  case "bytes"   => "Ljava/lang/Object;"
                         case "String"  => "Ljava/lang/String;"
      //Complex ------------------------ Not Supported in Salat-Avro?
  //                       case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
                    //     case "enum"    => 
                    //     case "array"   => 
                     //    case "map"     => 

//TODO the rest of this method
                      // case "union"   => classOf[]
                      // case "[null,"+_+"]"      => 
                      // case "[null,String]"      => classOf[Option[String]] 
                       //case "fixed"   => classOf[]
                     // case n: List[Any] => classOf[Option[Any]]         
                                             //other Scala datatypes
                         case "Byte"    => "Ljava/lang/Object;"
                         case "Short"   => "Ljava/lang/Object;"
                         case "Char"    => "Ljava/lang/Object;"
                         case "Any"     => "Ljava/lang/Object;"
                         case "AnyRef"  => "Ljava/lang/Object;"
                         case "Unit"    => "Ljava/lang/Object;"
                         case "Nothing" => "Ljava/lang/Object;"
                         case "Null"    => "Ljava/lang/Object;"
                         case "Object"  => "Ljava/lang/Object;"
                     //    case x: String =>  //if its a string but none of the above, its a nested record type

                         case _         => println("no unapply type for unsupported type"); error("no unapply type for unsupported type")//println("Avro Schemas only contain Primitive and Complex Avro types");
                        }
  }

  def getReturnInstr(fieldType: String): Int = {
    fieldType match {
                         //Primitive Avro types --- Thanks @ConnorDoyle for the type mapping
                   //    case "Null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "Boolean" => IRETURN
                         case "Int"     => IRETURN
                         case "Long"    => LRETURN
                         case "Float"   => FRETURN
                         case "Double"  => DRETURN
                         case "bytes"   => IRETURN//IRETURN is corrrect for bytes?
                         case "String"  => ARETURN
      //Complex ------------------------ Not Supported in Salat-Avro?
  //                       case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
                         case "enum"    => ARETURN
                         case "array"   => ARETURN
                         case "map"     => ARETURN
                      //   case "stream"  => ARETURN
                      //   case "option"  => ARETURN
                      // case "union"   => classOf[]
                      // case "[null,"+_+"]"      => 
                      // case "[null,String]"      => classOf[Option[String]] 
                       //case "fixed"   => classOf[]
                     // case n: List[Any] => classOf[Option[Any]]         
                                             //other Scala datatypes
                         case "Short"   => IRETURN
                         case "Byte"    => IRETURN
                         case "Char"    => IRETURN
                         case "Any"     => ARETURN
                         case "AnyRef"  => ARETURN
                         case "Unit"    => RETURN
                         case "Nothing" => ARETURN
                         case "Null"    => ARETURN
                         case "Object"  => ARETURN
                         case x: String => ARETURN //if its a string but none of the above, its a nested record type

                         case _         => ARETURN//println("Avro Schemas only contain Primitive and Complex Avro types");ARETURN
                        }
  }



  def getObject(fieldType: String) = {
    fieldType match {
                         //Primitive Avro types --- Thanks @ConnorDoyle for the type mapping
                   //    case "Null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "Boolean" => true.asInstanceOf[Object]
                         case "Int"     => 1.asInstanceOf[Object]
                         case "Long"    => 1L.asInstanceOf[Object]
                         case "Float"   => 1F.asInstanceOf[Object]
                         case "Double"  => 1D.asInstanceOf[Object]
                        // case "bytes"   => Array[Byte](1.toByte).asInstanceOf[Object]//Array is corrrect for bytes?
                         case "String"  => ""
      //Complex ------------------------ Not Supported in Salat-Avro?
  //                       case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
                      //   case "enum"    => 
                      //   case "array"   => 
                      //   case "map"     => 
                      // case "union"   => classOf[]
                      // case "[null,"+_+"]"      => 
                      // case "[null,String]"      => classOf[Option[String]] 
                       //case "fixed"   => classOf[]
                     // case n: List[Any] => classOf[Option[Any]]         
                         case "Short"   => 1.toShort.asInstanceOf[Object]
                         case "Byte"    => 1.toByte.asInstanceOf[Object]
                         case "Char"    => 'k'.asInstanceOf[Object]
                         case "Any"     => "".asInstanceOf[Any].asInstanceOf[Object]
                         case "AnyRef"  => "".asInstanceOf[AnyRef].asInstanceOf[Object]
                         case "Unit"    => ().asInstanceOf[scala.runtime.BoxedUnit]//Unit.asInstanceOf[Object]
                        // case "Nothing" => //now here's a conundrum wtf, 
                         case "Null"    => null.asInstanceOf[Object]
                         case "Object"  => new Object
                     //    case x: String =>  //if its a string but none of the above, its a nested record type

                       //  case _         => //println("Avro Schemas only contain Primitive and Complex Avro types");ARETURN
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
  def getLoadInstr(fieldType: String): Int = {
    fieldType match {
                         //Primitive Avro types --- Thanks @ConnorDoyle for the type mapping
                   //    case "Null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "Boolean" => ILOAD
                         case "Int"     => ILOAD
                         case "Long"    => LLOAD
                         case "Float"   => FLOAD
                         case "Double"  => DLOAD
                         case "bytes"   => ILOAD
                         case "String"  => ALOAD
      //Complex ------------------------ Not Supported in Salat-Avro?
  //                       case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
                      //   case "enum"    => ARETURN
                      //   case "array"   => ARETURN
                      //   case "map"     => ARETURN
                      // case "union"   => classOf[]
                      // case "[null,"+_+"]"      => 
                      // case "[null,String]"      => classOf[Option[String]] 
                       //case "fixed"   => classOf[]

                         case "Short"   => ILOAD
                         case "Byte"    => ILOAD
                         case "Char"    => ILOAD
                         case "Any"     => ALOAD
                         case "AnyRef"  => ALOAD
                         case "Unit"    => ALOAD
                         case "Nothing" => ALOAD
                         case "Null"    => ALOAD
                         case "Object"  => ALOAD

                         case x: String => ALOAD //
                         case _         => println("Avro Schemas only contain Primitive and Complex Avro types"); ALOAD
                        }
  }
def getReturnTypes(fieldSeeds: List[FieldSeed]) = {
fieldSeeds.map(n => n.fieldType).map(m => m match {
      //Primitive Avro types --- Thanks to @ConnorDoyle for suggesting the type mapping
      //    case "Null"    => classOf[Unit]
      case "Boolean" => classOf[Boolean]
      case "Int"     => classOf[Int]
      case "Long"    => classOf[Long]
      case "Float"   => classOf[Float]
      case "Double"  => classOf[Double]
      case "bytes"   => classOf[Seq[Byte]]
      case "String"  => classOf[String]
      //Complex ------------------------ Not Supported in Salat-Avro?
      //case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
      case "enum"    => classOf[Enumeration#Value]
      case "array"   => classOf[Seq[_]]
      case "map"     => classOf[Map[String, _]]
      case "Map(type -> record, name -> rec, doc -> , fields -> List(Map(name -> i, type -> List(int, null))))"     => classOf[Map[String, _]]
      // case "union"   => classOf[]
      // case "[null,"+_+"]"      => 
      // case "[null,String]"      => classOf[Option[String]] 
      //case "fixed"   => classOf[]


                         case "Short"   => classOf[Short]
                         case "Byte"    => classOf[Byte]
                         case "Char"    => classOf[Char]
                         case "Any"     => classOf[Any]
                         case "AnyRef"  => classOf[AnyRef]
                         case "Unit"    => classOf[scala.runtime.BoxedUnit]//classOf[Unit]
                         case "Nothing" => classOf[Nothing]
                         case "Null"    => classOf[Null]
                         case "Object"  => classOf[Object]
      //  case "option"   =>  classOf[Option[Any]]
      //     case n: List[Any] => classOf[Option[Any]]         
                         
      case x: String => x //if its a string but none of the above, its a nested record type
    //  case a:Any     => a// "Avro Schemas should only contain Primitive and Complex Avro types" 

    })
}


}
