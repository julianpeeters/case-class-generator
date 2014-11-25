package com.julianpeeters.caseclass.generator

import scala.collection.JavaConversions.JConcurrentMapWrapper
import java.util.concurrent.ConcurrentHashMap
import scala.reflect.runtime.universe._

object ClassStore {

  val generatedClasses: scala.collection.concurrent.Map[Type, DynamicCaseClass] = JConcurrentMapWrapper(new ConcurrentHashMap[Type, DynamicCaseClass]())

  def accept(dcc: DynamicCaseClass) {
    if (!generatedClasses.contains(dcc.tpe)) {
      generatedClasses += dcc.tpe -> dcc
    }
  }
}
