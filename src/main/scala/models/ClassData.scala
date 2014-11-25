package com.julianpeeters.caseclass.generator


case class ClassNamespace(namespace: Option[String])
case class ClassName(name: String)
case class ClassFieldData(fields: List[FieldData])

case class ClassData(
  classNamespace: ClassNamespace,
  className: ClassName,
  classFields: ClassFieldData)

