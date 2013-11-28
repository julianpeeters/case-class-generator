package caseclass.generator
//short-hand input format for debugging purposes
case class FieldSeed(fieldName: String, fieldType: String)
//most cases will read from a source and populate FieldData directly
case class FieldData(
  fieldName: String, 
  fieldType: String, 
  typeDescriptor: String,
  unapplyType: String,
  loadInstr: Int, 
  returnInstr: Int,
  asParam: Object)

