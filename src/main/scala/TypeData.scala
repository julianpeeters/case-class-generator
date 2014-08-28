package com.julianpeeters.caseclass.generator

case class TypeData(
  typeDescriptor: String, 
  unapplyType: String, 
  loadInstr: Int, 
  returnInstr: Int, 
  asParam: Object,
  unerasedType: String,
  unerasedTypeDescriptor: String)
