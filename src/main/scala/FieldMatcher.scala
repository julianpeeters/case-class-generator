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


                        case "option"   =>  Type.getDescriptor(classOf[Option[Any]])           
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
                         case "bytes"   => "Ljava/lang/Object;"
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
                         
                     //    case x: String =>  //if its a string but none of the above, its a nested record type

                         case _         => "unsupported Type"//println("Avro Schemas only contain Primitive and Complex Avro types");
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
                      // case "union"   => classOf[]
                      // case "[null,"+_+"]"      => 
                      // case "[null,String]"      => classOf[Option[String]] 
                       //case "fixed"   => classOf[]
                     // case n: List[Any] => classOf[Option[Any]]         
                         
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
                        // case "bytes"   => 1.asInstanceOf[Object]//IRETURN is corrrect for bytes?
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


      //  case "option"   =>  classOf[Option[Any]]
      //     case n: List[Any] => classOf[Option[Any]]         
                         
      case x: String => x //if its a string but none of the above, its a nested record type
    //  case a:Any     => a// "Avro Schemas should only contain Primitive and Complex Avro types" 

    })
}


}
