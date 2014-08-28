package com.julianpeeters.caseclass.generator

//User-facing
case class FieldSeed(fieldName: String, fieldType: String)

//FieldSeeds get enhanced with a typeData field when a DynamicCaseClass is made
case class FieldData(
  fieldName: String, 
  fieldType: String, 
  typeData: TypeData)




