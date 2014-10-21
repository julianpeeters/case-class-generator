package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

import java.util.Arrays
import scala.io.Codec._
case class Tupled(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "tupled", "()Lscala/Function1;", "()Lscala/Function1<Lscala/Tuple" + fieldData.length + "<" + fieldData.map(fd => fd.typeData.unapplyType).mkString + ">;L" + caseClassName + ";>;", null);
    mv.visitCode();
    mv.visitFieldInsn(GETSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
    mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "tupled", "()Lscala/Function1;");
    mv.visitInsn(ARETURN);
    mv.visitMaxs(1, 0);
    mv.visitEnd();
  }
}
