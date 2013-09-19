package avocet
import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

import java.io._

object Main extends App {

  val infile = new File("input.avro")
  val typeTemplate = (new CaseClassGenerator(infile).typeTemplate)
  type MyRecord = typeTemplate.type 

  val dbo = grater[MyRecord].asDBObject(typeTemplate)
    println(dbo)

  val obj = grater[MyRecord].asObject(dbo)
    println(obj)
 
  println(typeTemplate == obj)
}






