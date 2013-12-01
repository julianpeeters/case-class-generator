package caseclass.generator
import scala.util.parsing.json._

class JSONParser(jsonSchema: String) {

 val parsedJSON = List((JSON.parseFull(jsonSchema)).get)

  val classSeeds = asSchemaList(parsedJSON)//extracts nested schemas to a list of schemas
    .filter(s => s != List.empty)
    .map(schema => ClassData(
      getNamespace(parsedJSON), 
      getName(schema), 
      getFields(schema), 
      getInstantiationTypes(schema)))


//How can I make implicit everything below this line
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
      case m: Map[_, _] => true //hope for a [String, Any]
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
   
  def getFields(schema: Any): List[FieldSeed]= {//List[Map[String, Any]]= {

    def matchTypes(JSONfieldType: Any): Any = {
      JSONfieldType match {
        case u: List[(Any, Null)] => U(u) = u; List("option", u(0)) //u(0) // U(u) = u//u.asInstanceOf[Option[Int]]//"union"
        case s: String            => S(s) = s; s //if the type is a nested record, getDescriptor returns wrong value anyways
        case m: Map[String, Any]  => m("name")   //
        case c: Class[_]        => C(c) = c
        case _                    => println("none of the above")
      }
    }

    def avroToScalaType(fieldType: String): String = {
      fieldType match {
//Thanks to @ConnorDoyle for the mapping: https://github.com/GenslerAppsPod/scalavro
      case "null"    => "Unit"
      case "boolean" => "Boolean"
      case "int"     => "Int"
      case "long"    => "Float"
      case "float"    => "Long"
      case "double"    => "Double"
      case "string"   => "String"
      }
    }




    for {
      M(schema) <- List(schema)
      L(fields) = schema("fields")
      M(field) <- fields
      S(fieldName) = field("name")
      S(fieldType) = matchTypes(field("type"))
    } yield FieldSeed(fieldName, avroToScalaType(fieldType))
 
  }

//TODO Add support for nested custom classes
/*
def matchTypes(JSONfieldType: Any, modelClass: Object) = {//: java.lang.Class[_ <: Object] = {
      JSONfieldType match {
      //  case u: List[(Any, Null)] => U(u) = u; List("option", u(0)) //u(0) // U(u) = u//u.asInstanceOf[Option[Int]]//"union"
      //  case s: String            => S(s) = s; modelClass.getClass() //if the type is a nested record, getDescriptor returns wrong value anyways
       // case m: Map[String, Any]  => m("name")   //
       // case c: Class[Any]        => C(c) = c; c
       // case _                    => Class[Any]

        case "Int" => classOf[Int]
        case "rec" => modelClass
        case _     => 
      }
    }
*/

  def getInstantiationTypes(schema: Any) = {
    val ft = getFields(schema).map(n => n.fieldType)
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
      case "Map(type -> record, name -> rec, doc -> , fields -> List(Map(name -> i, type -> List(int, null))))"     => classOf[Map[String, _]]
      // case "union"   => classOf[]
      // case "[null,"+_+"]"      => 
      // case "[null,String]"      => classOf[Option[String]] 
      //case "fixed"   => classOf[]


      //  case "option"   =>  classOf[Option[Any]]
      //     case n: List[Any] => classOf[Option[Any]]         
                         
      case x: String => x //if its a string but none of the above, its a nested record type
      case a:Any     => a

    })
  }
}
