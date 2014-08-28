package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class ModuleCinit(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String) {
  def dump = {
    mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_STATIC, "<clinit>", "()V", null, null);
    mv_MODULE.visitCode();
    mv_MODULE.visitTypeInsn(NEW, caseClassName + "$");
    mv_MODULE.visitMethodInsn(INVOKESPECIAL, caseClassName + "$", "<init>", "()V");
    mv_MODULE.visitInsn(RETURN);
    mv_MODULE.visitMaxs(1, 0);
    mv_MODULE.visitEnd();

  }
}
