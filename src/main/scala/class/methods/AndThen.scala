package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

case class AndThen(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[EnrichedField]) {
  def dump = {

    mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "andThen", "(Lscala/Function1;)Lscala/Function1;", "<A:Ljava/lang/Object;>(Lscala/Function1<L" + caseClassName + ";TA;>;)Lscala/Function1<" + fieldData.map(fd => fd.typeData.unerasedType).head + "TA;>;", null);

    mv.visitCode();
    mv.visitFieldInsn(GETSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "andThen", "(Lscala/Function1;)Lscala/Function1;");
    mv.visitInsn(ARETURN);
    mv.visitMaxs(2, 1);
    mv.visitEnd();

  }
}
