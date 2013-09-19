package avocet
import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import org.apache.avro.Schema

import org.objectweb.asm._
import Opcodes._

import java.io._
import scala.util.parsing.json._

class CaseClassGenerator(infile: File) {


  def asSchemaFromFile(infile: File): Schema = {
    val bufferedInfile = scala.io.Source.fromFile(infile, "iso-8859-1")
    val parsable = new String(bufferedInfile.getLines.mkString.dropWhile(_ != '{').toCharArray)
    val avroSchema = new Schema.Parser().parse(parsable)
    avroSchema
  }


 
  val avroSchema = asSchemaFromFile(infile)
println("schema:  " +avroSchema)


  val jsonSchema = List((JSON.parseFull(avroSchema.toString)).get)
  //  println(jsonSchema)

  class jsonTypeConverter[T]{
    def unapply(a:Any): Option[T] = Some(a.asInstanceOf[T])
    def update(a:Any): Option[T] = Some(a.asInstanceOf[T])
  }

  object M extends jsonTypeConverter[Map[String, Any]]
  object L extends jsonTypeConverter[List[Any]]
  object S extends jsonTypeConverter[String]
  object I extends jsonTypeConverter[Int]
 // object D extends jsonTypeConverter[Double]
 // object B extends jsonTypeConverter[Boolean]
  object C extends jsonTypeConverter[java.lang.Class[Any]]

  object O extends jsonTypeConverter[Option[Any]]
  object U extends jsonTypeConverter[List[(Any, Null)]]

    val classSeeds = asSchemaList(jsonSchema)
    .filter(s => s != List.empty)
    .map(schema => ClassData(getNamespace(jsonSchema), 
      getName(schema), 
      getFields(schema), 
      getInstantiationTypes(schema)))//Map("namespace" -> getNamespace(jsonSchema),"name" -> getName(schema),"fields" -> getValueMembers(schema)))

  //classSeeds.foreach(println)






  def asSchemaList(jsonSchema: List[Any]): List[Any]= { 
    if (jsonSchema == List.empty) List(List.empty)
    else {
      val nestedSchemas = asSchemaList(getNestedSchemas(jsonSchema))
      List(jsonSchema, nestedSchemas).flatten
    } 
  }

  def getNestedSchemas(jsonSchema: List[Any]) =  for {
    M(record) <- jsonSchema
    L(fields) = record("fields")
    M(field) <- fields
    fieldType = field("type")

    if {fieldType match {
      case m: Map[String, Any] => true
      case _                   => false
    }}

    } yield fieldType

  def getNamespace(jsonSchema: List[Any]): String = (for { 
    (M(map)) <- jsonSchema
    S(namespace) = map("namespace")
  } yield namespace).head

  def getName(schema: Any): String = (for { 
     M(map) <- List(schema)
     S(name) = map("name")
  } yield name).head
   
  def getFields(schema: Any): List[FieldData]= {//List[Map[String, Any]]= {
    def matchTypes(JSONfieldType: Any): Any = {
      JSONfieldType match {
        case u: List[(Any, Null)] => U(u) = u; List("option", u(0)) //u(0) // U(u) = u//u.asInstanceOf[Option[Int]]//"union"
        case s: String            => S(s) = s; s //if the type is a nested record, getDescriptor returns wrong value anyways
        case m: Map[String, Any]  => m("name")   //
        case c: Class[Any]        => C(c) = c
        case _                    => println("none of the above")
      }
    }
    for {
      M(schema) <- List(schema)
      L(fields) = schema("fields")
      M(field) <- fields
      S(fieldName) = field("name")
      S(fieldType) = matchTypes(field("type"))
    } yield FieldData(
        fieldName, 
        fieldType, 
        toTypeDescriptor(fieldType),
        getUnapplyType(fieldType),
        getLoadInstr(fieldType), 
        getReturnInstr(fieldType) )//Map("name" -> fieldName, "type" -> toType(fieldType), "typeDescriptor" -> toTypeDescriptor(fieldType), "loadInstruction" -> toBytecodeLoadInstruction(fieldType)) 
  }





//TODO Pull the matches out of the map so I can see the tuple I"m creating
def getInstantiationTypes(schema: Any) = {

  val ft = getFields(schema).map(n => n.fieldType)
    ft.map(m => m match {
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
  //                       case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
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
                         case a:Any     => a// "Avro Schemas should only contain Primitive and Complex Avro types" 

                        })
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
                         
                         case x: String => "L"+ x+ ";" //if its a string but none of the above, its a nested record type

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

/*
  def convertJSONSchemaTypes(fields: List[(String, Any)]) = {
    //Tuple each value member, with fields to represent (name, type, typeDescriptor, BytecodeReturnType, BytecodeLoadInstruction)
    fields.map(n => (n._1,                             // _1 = name
             	     n._2,                             // _2 = type as String
                     toTypeDescriptor(n._2.toString).toString  // _3 = typeDescriptor
                 //    toBytecodeReturnType(n._2).toString.toInt,       // _4 = BytecodeReturnType
                 ))//    toBytecodeLoadInstruction(n._2).toString.toInt) )// _5 = BytecodeLoadInstruction
  }
*/


    def matchTypes(JSONfieldType: Any, modelClass: Object) = {//: java.lang.Class[_ <: Object] = {
      JSONfieldType match {
      //  case u: List[(Any, Null)] => U(u) = u; List("option", u(0)) //u(0) // U(u) = u//u.asInstanceOf[Option[Int]]//"union"
      //  case s: String            => S(s) = s; modelClass.getClass() //if the type is a nested record, getDescriptor returns wrong value anyways
       // case m: Map[String, Any]  => m("name")   //
       // case c: Class[Any]        => C(c) = c; c
       // case _                    => Class[Any]

        case "int" => classOf[Int]
        case "rec" => modelClass
        case _     => 
      }
    }


val typeTemplate = classSeeds.map(classSeed => new DynamicCaseClass(classSeed)).map(cls => cls.instantiated$).head//get a top-level record


}

