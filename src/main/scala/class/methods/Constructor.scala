package com.julianpeeters.caseclass.generator

import org.objectweb.asm._
import Opcodes._
import scala.reflect.runtime.universe._

import java.util.Arrays
import scala.io.Codec._

case class Constructor(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[EnrichedField]) {

  def dump = {
    fieldData.foreach(fd => {
      val tpe = {
        if (fd.typeData.typeDescriptor == "Lscala/runtime/BoxedUnit;") "V"
        else fd.typeData.typeDescriptor
      }

      mv = cw.visitMethod(ACC_PUBLIC, fd.fieldName, "()" + tpe, fd.typeData.unerasedType, null);
      mv.visitCode();
      if (!(fd.fieldType =:= typeOf[Unit])) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, caseClassName, fd.fieldName, fd.typeData.typeDescriptor.toString);
      }
      mv.visitInsn(fd.typeData.returnInstr);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    })
  }
}
