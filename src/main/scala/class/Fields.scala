package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class Fields(cw: ClassWriter, fieldData: List[FieldData]) {
  def dump = {
    fieldData.foreach(fd=>(cw.visitField(ACC_PRIVATE + ACC_FINAL, fd.fieldName, fd.typeData.typeDescriptor.toString, fd.typeData.unerasedType, null).visitEnd())) 
  }
}
