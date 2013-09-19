import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import org.apache.avro.Schema

import org.objectweb.asm._
import Opcodes._

import java.io._
import scala.util.parsing.json._

case class ClassData(
  classNamespace: String, 
  className: String,
  classFields: List[FieldData], 
  returnType: List[Any] )

case class FieldData(
  fieldName: String, 
  fieldType: Any, 
  typeDescriptor: Any, 
  loadInstr: Int, 
  returnInstr: Int)

object Main extends App {




val infile = new File("input.avro")

  def asSchemaFromFile(infile: File): Schema = {
    val bufferedInfile = scala.io.Source.fromFile(infile, "iso-8859-1")
    val parsable = new String(bufferedInfile.getLines.mkString.dropWhile(_ != '{').toCharArray)
    val schema = new Schema.Parser().parse(parsable)
    schema
 }
  val schema = asSchemaFromFile(infile)
println("schema:  " +schema)


  val jsonSchema = List((JSON.parseFull(schema.toString)).get)
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

    val descriptorsMaps = asSchemaList(jsonSchema)
    .filter(s => s != List.empty)
    .map(schema => ClassData(getNamespace(jsonSchema), 
      getName(schema), 
      getFields(schema), 
      getInstantiationTypes(schema)))//Map("namespace" -> getNamespace(jsonSchema),"name" -> getName(schema),"fields" -> getValueMembers(schema)))

  descriptorsMaps.foreach(println)






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






//val classDescriptors = ClassData()

 //object MyRecord {
    val myRecordDump = new MyRecordDump 
    val bytecodes = myRecordDump.dump//(classDescriptors)
    val model  = DynamicClassLoader.loadClass("models.MyRecord", bytecodes(0)) // load the class
    val model$ = DynamicClassLoader.loadClass("models.MyRecord$", bytecodes(1)) //load the module class
/*
    def apply(x: String, y: Int, z: Boolean) = {
      val method_apply = model$.getMethod("apply", classOf[String], classOf[Int], classOf[Boolean])//populate the instance with values,
      val classInstance = model$.getConstructor().newInstance()//getInstance(module)
      method_apply.invoke(classInstance, x.asInstanceOf[Object], y: java.lang.Integer, z: java.lang.Boolean)//roughly equivalent to using the statement `MyRecord(fieldValue)`
    }

    val typeTemplate = MyRecord("hello runtime", 1, true) //Rename def to MyRecord to preserve the look and feel of salat??
    type MyRecord = typeTemplate.type
 */
/*
    def apply(x: Any, y: Any) = {
      val method_apply = model$.getMethod("apply", x.getClass, y.getClass)//populate the instance with values,
      val classInstance = model$.getConstructor().newInstance()//getInstance(module)
      method_apply.invoke(classInstance, x.asInstanceOf[Object], y.asInstanceOf[Object])
    }
    val typeTemplate = MyRecord("hello", " runtime") //Rename def to MyRecord to preserve the look and feel of salat??
  //  val typeTemplate = MyRecord("hello runtime", 1, true) //Rename def to MyRecord to preserve the look and feel of salat??
    type MyRecord = typeTemplate.type


*/
/*
   // def apply(fieldValue: Any) = {
println()
      val methodParams: List[Class[_]] = List(fieldValue.getClass)//, classOf[Int], classOf[Boolean])
      val method_apply = model$.getMethod("apply", methodParams: _*)//populate the instance with values,
      val instance = model$.getConstructor().newInstance()//getInstance(module)
      val insantiationParams: List[Object] = List(fieldValue.asInstanceOf[Object])
      method_apply.invoke(instance, insantiationParams: _*)
   // }
    val typeTemplate = MyRecord("hello runtime") //Rename def to MyRecord to preserve the look and feel of salat??
  //  val typeTemplate = MyRecord("hello runtime", 1, true) //Rename def to MyRecord to preserve the look and feel of salat??
    type MyRecord = typeTemplate.type
*/




   // def apply(fieldValue: Any) = {
println()
      val methodParams: List[Class[_]] = List(classOf[String], classOf[Int], classOf[Boolean])
      val method_apply = model$.getMethod("apply", methodParams: _*)//populate the instance with values,
      val instance$ = model$.getConstructor().newInstance()//getInstance(module)
 //     val insantiationParams: List[Object] = List("", 1.asInstanceOf[Object], true.asInstanceOf[Object] )
      val insantiationParams: List[Object] = List("", 1.asInstanceOf[Object], true.asInstanceOf[Object] )
     // method_apply.invoke(instance, insantiationParams: _*)
   // }
   // val typeTemplate = MyRecord("hello runtime") //Rename def to MyRecord to preserve the look and feel of salat??
  //  val typeTemplate = MyRecord("hello runtime", 1, true) //Rename def to MyRecord to preserve the look and feel of salat??
    val typeTemplate = model$.getMethod("apply", methodParams: _*).invoke(instance$,  insantiationParams: _*)
    type MyRecord = typeTemplate.type



  //}
/*
  val modelClass$ = DynamicClassLoader.loadClass("models.MyRecord$", MyRecord$Dump.dump()) //Load the module class
  val modelClass  = DynamicClassLoader.loadClass("models.MyRecord", MyRecordDump.dump())   //Then load the "real" class

  val instance$  = modelClass$.getConstructor().newInstance()//Changed Module$Dump <init> from PRIVATE to PUBLIC
  val methodParams: List[Class[_]] = List(classOf[String], classOf[Int], classOf[Boolean])
  val insantiationParams: List[Object] = List("", 1.asInstanceOf[Object], java.lang.Boolean.TRUE)

  
  //get a "type model" to use as the type for our dynamically generated case class
  val myRecord = modelClass$.getMethod("apply", methodParams: _*).invoke(instance$,  insantiationParams: _*)
 
  type MyRecord = myRecord.type with com.novus.salat.CaseClass//Compiler must trust us that asm gives a case class!

*/

  
  val dbo = grater[MyRecord].asDBObject(typeTemplate)

    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
  println(typeTemplate == obj)
  // println(MyRecord.typeTemplate == obj)
}




