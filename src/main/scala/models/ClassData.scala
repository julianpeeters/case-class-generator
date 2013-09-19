package models

case class ClassData(
  classNamespace: String, 
  className: String, 
  classFields: List[FieldData], 
  returnType: List[Any] )

