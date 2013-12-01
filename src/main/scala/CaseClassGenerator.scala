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
    println("adding " + dcc + " to the list of classes")
println(dcc.name)
    if (!generatedClasses.contains(dcc.fullName)) {
      generatedClasses += dcc.name -> dcc
    }
  }

/*
  def accept(dcc: DynamicCaseClass) = {
    accept(dcc)
    generatedClasses += (dcc.model.getName.toString -> dcc)
  }
*/

  def asClass(classSeed: ClassData) = {
    new DynamicCaseClass(classSeed).model
  }

  def asClass(json: String) = {
    val classSeed = new JSONParser(json).classSeeds.head
    new DynamicCaseClass(classSeed).model
  }




  def asInstance(classSeed: ClassData) = {
    new DynamicCaseClass(classSeed).instantiated$
  }

  def asInstance(json: String) = {
//    val classSeed = new JSONParser(json).classSeed.head
//    new DynamicCaseClass(classSeeds.head).instantiated$
    val classSeeds = new JSONParser(json).classSeeds
println("caseclassgenerator classSeeds " )
classSeeds.foreach(println)

    //classSeeds.map(seed => new DynamicCaseClass(seed).instantiated$)
    classSeeds.reverse.map(seed => new DynamicCaseClass(seed).instantiated$)


  }



}
