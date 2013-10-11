package avocet
import org.objectweb.asm._
import Opcodes._
import models._

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
                   //    case "null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "boolean" => Type.getDescriptor(classOf[Boolean])
                         case "int"     => Type.getDescriptor(classOf[Int])
                         case "long"    => Type.getDescriptor(classOf[Long])
                         case "float"   => Type.getDescriptor(classOf[Float])
                         case "double"  => Type.getDescriptor(classOf[Double])

                         case "bytes"   => Type.getDescriptor(classOf[Seq[Byte]])
                         case "string"  => Type.getDescriptor(classOf[String])
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
                         case "byte"    => Type.getDescriptor(classOf[Byte])
                         case "short"    => Type.getDescriptor(classOf[Short])
                         case "char"    => Type.getDescriptor(classOf[Char])
                         case "any"    => Type.getDescriptor(classOf[Any])
                         case "anyref"    => Type.getDescriptor(classOf[AnyRef])
                         case "unit"    => Type.getDescriptor(classOf[Unit])
                         case "nothing"    => Type.getDescriptor(classOf[Nothing])
                         case "null"    => Type.getDescriptor(classOf[Null])
                         case "object"    => Type.getDescriptor(classOf[Object])
                       // case "option"   =>  Type.getDescriptor(classOf[Option[Any]])           
                        ///   case n: List[Any] => classOf[Option[Any]]         
                         
                         case x: String => "L"+ x + ";" //if its a string but none of the above, its a nested record type

                         case _         => "Avro Schemas should only contain Primitive and Complex Avro types"
                        }).toString
  }

  def getUnapplyType(fieldType: String): String = {
    fieldType match {
                         //Primitive Avro types --- Thanks @ConnorDoyle for the type mapping
                   //    case "null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "boolean" => "Ljava/lang/Object;"
                         case "int"     => "Ljava/lang/Object;"
                         case "long"    => "Ljava/lang/Object;"
                         case "float"   => "Ljava/lang/Object;"
                         case "double"  => "Ljava/lang/Object;"
                       //  case "bytes"   => "Ljava/lang/Object;"
                         case "string"  => "Ljava/lang/String;"
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
                         case "byte"    => "Ljava/lang/Object;"
                         case "short"   => "Ljava/lang/Object;"
                         case "char"    => "Ljava/lang/Object;"
                         case "any"     => "Ljava/lang/Object;"
                         case "anyref"  => "Ljava/lang/Object;"
                         case "unit"    => "Ljava/lang/Object;"
                         case "nothing" => "Ljava/lang/Object;"
                         case "null"    => "Ljava/lang/Object;"
                         case "object"  => "Ljava/lang/Object;"
                     //    case x: String =>  //if its a string but none of the above, its a nested record type

                         case _         => println("no unapply type for unsupported type"); error("no unapply type for unsupported type")//println("Avro Schemas only contain Primitive and Complex Avro types");
                        }
  }

  def getReturnInstr(fieldType: String): Int = {
    fieldType match {
                         //Primitive Avro types --- Thanks @ConnorDoyle for the type mapping
                   //    case "null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "boolean" => IRETURN
                         case "int"     => IRETURN
                         case "long"    => LRETURN
                         case "float"   => FRETURN
                         case "double"  => DRETURN
                         case "bytes"   => IRETURN//IRETURN is corrrect for bytes?
                         case "string"  => ARETURN
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
                         case "short"   => IRETURN
                         case "byte"    => IRETURN
                         case "char"    => IRETURN
                         case "any"     => ARETURN
                         case "anyref"  => ARETURN
                         case "unit"    => RETURN
                         case "nothing" => ARETURN
                         case "null"    => ARETURN
                         case "object"  => ARETURN
                         case x: String => ARETURN //if its a string but none of the above, its a nested record type

                         case _         => ARETURN//println("Avro Schemas only contain Primitive and Complex Avro types");ARETURN
                        }
  }



  def getObject(fieldType: String) = {
    fieldType match {
                         //Primitive Avro types --- Thanks @ConnorDoyle for the type mapping
                   //    case "null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "boolean" => true.asInstanceOf[Object]
                         case "int"     => 1.asInstanceOf[Object]
                         case "long"    => 1L.asInstanceOf[Object]
                         case "float"   => 1F.asInstanceOf[Object]
                         case "double"  => 1D.asInstanceOf[Object]
                        // case "bytes"   => Array[Byte](1.toByte).asInstanceOf[Object]//Array is corrrect for bytes?
                         case "string"  => ""
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
                         case "short"   => 1.toShort.asInstanceOf[Object]
                         case "byte"    => 1.toByte.asInstanceOf[Object]
                         case "char"    => 'k'.asInstanceOf[Object]
                         case "any"     => "".asInstanceOf[Any].asInstanceOf[Object]
                         case "anyref"  => "".asInstanceOf[AnyRef].asInstanceOf[Object]
                         case "unit"    => Unit.asInstanceOf[Object]
                        // case "nothing" => //now here's a conundrum
                         case "null"    => null.asInstanceOf[Object]
                         case "object"  => new Object
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
value, i.e. object and array references.
*/
  def getLoadInstr(fieldType: String): Int = {
    fieldType match {
                         //Primitive Avro types --- Thanks @ConnorDoyle for the type mapping
                   //    case "null"    => (Type.getDescriptor(classOf[Unit]), )
                         case "boolean" => ILOAD
                         case "int"     => ILOAD
                         case "long"    => LLOAD
                         case "float"   => FLOAD
                         case "double"  => DLOAD
                         case "bytes"   => ILOAD
                         case "string"  => ALOAD
      //Complex ------------------------ Not Supported in Salat-Avro?
  //                       case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
                      //   case "enum"    => ARETURN
                      //   case "array"   => ARETURN
                      //   case "map"     => ARETURN
                      // case "union"   => classOf[]
                      // case "[null,"+_+"]"      => 
                      // case "[null,String]"      => classOf[Option[String]] 
                       //case "fixed"   => classOf[]

                         case "short"   => ILOAD
                         case "byte"    => ILOAD
                         case "char"    => ILOAD
                         case "any"     => ALOAD
                         case "anyref"  => ALOAD
                         case "unit"    => ALOAD
                         case "nothing" => ALOAD
                         case "null"    => ALOAD
                         case "object"  => ALOAD

                         case x: String => ALOAD //
                         case _         => println("Avro Schemas only contain Primitive and Complex Avro types"); ALOAD
                        }
  }
def getReturnTypes(fieldSeeds: List[FieldSeed]) = {
fieldSeeds.map(n => n.fieldType).map(m => m match {
      //Primitive Avro types --- Thanks to @ConnorDoyle for suggesting the type mapping
      //    case "null"    => classOf[Unit]
      case "boolean" => classOf[Boolean]
      case "int"     => classOf[Int]
      case "long"    => classOf[Long]
      case "float"   => classOf[Float]
      case "double"  => classOf[Double]
      case "bytes"   => classOf[Seq[Byte]]
      case "string"  => classOf[String]
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


                         case "short"   => classOf[Short]
                         case "byte"    => classOf[Byte]
                         case "char"    => classOf[Char]
                         case "any"     => classOf[Any]
                         case "anyref"  => classOf[AnyRef]
                         case "unit"    => classOf[Unit]
                         case "nothing" => classOf[Nothing]
                         case "null"    => classOf[Null]
                         case "object"  => classOf[Object]
      //  case "option"   =>  classOf[Option[Any]]
      //     case n: List[Any] => classOf[Option[Any]]         
                         
      case x: String => x //if its a string but none of the above, its a nested record type
    //  case a:Any     => a// "Avro Schemas should only contain Primitive and Complex Avro types" 

    })
}


}
