package com.julianpeeters.caseclass.generator

import scala.collection.JavaConversions.JConcurrentMapWrapper
import java.util.concurrent.ConcurrentHashMap

object ClassStore {

  val generatedClasses: scala.collection.concurrent.Map[String, DynamicCaseClass] = JConcurrentMapWrapper(new ConcurrentHashMap[String, DynamicCaseClass]())

  def accept(dcc: DynamicCaseClass) {
    if (!generatedClasses.contains(dcc.fullName)) {
      generatedClasses += dcc.name -> dcc
    }
  }
}
