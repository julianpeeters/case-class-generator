package models

case class FieldData(
  fieldName: String, 
  fieldType: String, 
  typeDescriptor: String,
  unapplyType: String,
  loadInstr: Int, 
  returnInstr: Int,
  asParam: Object)

