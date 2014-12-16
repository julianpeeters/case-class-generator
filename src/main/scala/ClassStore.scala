package com.julianpeeters.caseclass.generator


import java.util.concurrent.ConcurrentHashMap
import scala.reflect.runtime.universe._

object ClassStore {

  val generatedClasses: scala.collection.concurrent.Map[Type, DynamicCaseClass] = scala.collection.convert.Wrappers.JConcurrentMapWrapper(new ConcurrentHashMap[Type, DynamicCaseClass]())

  def accept(dcc: DynamicCaseClass) {
    if (!generatedClasses.contains(dcc.tpe)) {
      generatedClasses += dcc.tpe -> dcc
    }
  }
}
