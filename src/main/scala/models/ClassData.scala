
package caseclass.generator

case class ClassData(
//  classNamespace: String, 
  classNamespace: Option[String], 
  className: String, 
  classFields: List[FieldSeed])


