package com.julianpeeters.caseclass.generator

//User-facing
case class FieldData(fieldName: String, fieldType: String)

//FieldSeeds get enhanced with a typeData field when a DynamicCaseClass is made
case class TypedFields(
  fieldName: String,
  fieldType: String,
  typeData: TypeData)

