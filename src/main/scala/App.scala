package avocet
import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

object Main extends App {
//usually we'd be reading from a source
 // val infile = new File("input.avro")
 // val typeTemplate = CaseClassGenerator.parseFromFile(infile)//instantiated module class

//but for now lets make it easy debug my Scala signature issue (chokes on > 3 fields even tho sig bytes are ok before encoding)
  val valueMembers: List[FieldSeed] = List(FieldSeed("a","int"), FieldSeed("b","int"))//, FieldSeed("d","boolean"))
  val classData = ClassData("models", "MyRecord", valueMembers, FieldMatcher.getReturnTypes(valueMembers))
  val typeTemplate = CaseClassGenerator.make(classData)

  type MyRecord = typeTemplate.type

  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
  println(typeTemplate == obj)
}






