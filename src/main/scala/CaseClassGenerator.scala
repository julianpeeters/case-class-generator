package caseclass.generator
import artisinal.pickle.maker._
import org.objectweb.asm._
import Opcodes._

import java.io._
import scala.util.parsing.json._

object CaseClassGenerator { 

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
    val classSeed = new JSONParser(json).classSeeds.head
    new DynamicCaseClass(classSeed).instantiated$
  }

}
