package com.julianpeeters.caseclass.generator

import scala.reflect.runtime.universe._


//User-facing
case class FieldData(fieldName: String, fieldType: Type)

//Field seeds get enhanced with a typeData field when a DynamicCaseClass is made
case class EnrichedField(
  fieldName: String,
  fieldType: Type,
  typeData: TypeData)

