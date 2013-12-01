package caseclass.generator

//short-hand input format for debugging purposes
case class FieldSeed(fieldName: String, fieldType: String)
//most cases will read from a source and populate FieldData directly
case class FieldData(
  fieldName: String, 
  fieldType: String, 
  typeData: TypeData)
  //asParam: Object)




