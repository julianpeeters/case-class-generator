
package models

case class ClassData(
  classNamespace: String, 
  className: String, 
  classFields: List[FieldSeed], 
  returnType: List[Object] )

