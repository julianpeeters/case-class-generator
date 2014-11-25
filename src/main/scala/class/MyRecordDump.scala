package com.julianpeeters.caseclass.generator

import artisanal.pickle.maker._
import org.objectweb.asm._
import Opcodes._

import scala.reflect.internal.pickling.ByteCodecs
import scala.reflect.ScalaSignature

object MyRecordDumper {

  def dump(mySig: artisanal.pickle.maker.ScalaSig, caseClassName: String, fieldData: List[EnrichedField]): Array[Byte] = {

    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS) //, ClassWriter.COMPUTE_FRAMES); //now visit max's args don't matter
    var fv: FieldVisitor = null
    var mv: MethodVisitor = null
    var av0: AnnotationVisitor = null
    val ctorReturnType = "(" + fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ")V"

    ClassHeader(cw, caseClassName).dump
    ScalaSigAnnotation(cw, av0, mySig).dump
    Fields(cw, fieldData).dump

    if (fieldData.length == 1) {
      AndThen(cw, mv, caseClassName, fieldData).dump;
      Compose(cw, mv, caseClassName, fieldData).dump;
    }
    else {
      Tupled(cw, mv, caseClassName, fieldData).dump;
      Curried(cw, mv, caseClassName).dump
    }

    Constructor(cw, mv, caseClassName, fieldData).dump 

    val name = {
      if (caseClassName.contains('/')) caseClassName.dropWhile(c => c != '/').drop(1)
      else caseClassName
    }
    Copy(cw, mv, caseClassName, fieldData).dump
    CopyDefault(cw, mv, caseClassName, fieldData).dump
    ProductPrefix(cw, mv, name, fieldData).dump
    ProductArity(cw, mv, fieldData).dump
    ProductElement(cw, mv, caseClassName, fieldData).dump
    ProductIterator(cw, mv).dump
    CanEqual(cw, mv, caseClassName).dump
    HashCode(cw, mv, caseClassName, fieldData).dump
    ToString(cw, mv).dump
    Equals(cw, mv, caseClassName, fieldData).dump
    Init(cw, mv, caseClassName, fieldData, ctorReturnType).dump

    cw.toByteArray()
  }
}
