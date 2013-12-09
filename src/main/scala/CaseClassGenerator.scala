package caseclass.generator
import artisinal.pickle.maker._
import org.objectweb.asm._
import Opcodes._

import java.io._
import scala.util.parsing.json._

import scala.collection.JavaConversions.JConcurrentMapWrapper
import java.util.concurrent. ConcurrentHashMap



object CaseClassGenerator {

  val generatedClasses: scala.collection.concurrent.Map[String, DynamicCaseClass] = JConcurrentMapWrapper(new ConcurrentHashMap[String, DynamicCaseClass]())

  def accept(dcc: DynamicCaseClass) {
    if (!generatedClasses.contains(dcc.fullName)) {
      generatedClasses += dcc.name -> dcc
    }
  }

  def asClass(classSeed: ClassData) = {
    new DynamicCaseClass(classSeed).model
  }

  def asClass(json: String) = {
    val classSeed = JSONParser.parseJsonString(json).head
    new DynamicCaseClass(classSeed).model
  }




  def asInstance(classSeed: ClassData) = {
//    new DynamicCaseClass(classSeed).instantiated$
    new DynamicCaseClass(classSeed)
  }

  def asInstance(json: String) = {
//    val classSeed = new JSONParser(json).classSeed.head
//    new DynamicCaseClass(classSeeds.head)//.instantiated$
    val classSeeds = JSONParser.parseJsonString(json)
println("caseclassgenerator classSeeds " )
classSeeds.foreach(println)

    //classSeeds.map(seed => new DynamicCaseClass(seed).instantiated$)
//    classSeeds.reverse.map(seed => new DynamicCaseClass(seed).instantiated$)
    classSeeds.reverse.map(seed => new DynamicCaseClass(seed)).reverse.head


  }



}
