package caseclass.generator
import scala.util.parsing.json._

object JSONParser {

  def parseJsonString(jsonSchema: String) = {
    
    val parsedJSON = List((JSON.parseFull(jsonSchema)).get)

    asSchemaList(parsedJSON)//extracts nested schemas to a list of schemas (parsed, so really a list of ClassData objects)
      .filter( s => s != List.empty)
      .map(schema => ClassData( 
        getNamespace(parsedJSON), 
        getName(schema), 
        getFields(schema)) )
  }

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
      case m: Map[String, Any] => true
      case _                   => false
    }}
    } yield fieldType

  def getNamespace(jsonSchema: List[Any]): Option[String] = Option((for { 
    (M(map)) <- jsonSchema
//    S(namespace) = {
    S(namespace) = {
      if (Option(map("namespace")).isDefined) map("namespace")
      else None
    } 
  } yield namespace).head)//.replaceAllLiterally(".", "/")//.takeRight(4)

  def getName(schema: Any): String = (for { 
     M(map) <- List(schema)
     S(name) = map("name")
  } yield name).head
   
  def getFields(schema: Any): List[FieldSeed]= {//List[Map[String, Any]]= {

    def matchTypes(JSONfieldType: Any): Any = {
      JSONfieldType match {
        case u: List[(Any, Null)] => U(u) = u; List("option", u(0)) //u(0) // U(u) = u//u.asInstanceOf[Option[Int]]//"union"
        case s: String            => S(s) = s; s //if the type is a nested record, getDescriptor returns wrong value anyways
        case m: Map[String, Any]  => m("name")   
        case c: Class[Any]        => C(c) = c
        case _                    => error("JSON field type not found")
      }
    }

    def avroToScalaType(fieldType: String): String = {
      fieldType match {
//Thanks to @ConnorDoyle for the mapping: https://github.com/GenslerAppsPod/scalavro
      case "null"    => "Unit"
      case "boolean" => "Boolean"
      case "int"     => "Int"
      case "long"    => "Long"
      case "float"   => "Float"
      case "double"  => "Double"
      case "string"  => "String"



      case x: String => println("JSON paser foun a " + x) ; x
      case _         => error("JSON parser found an unsupported type")
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



}
