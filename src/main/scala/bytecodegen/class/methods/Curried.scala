package caseclass.generator
import artisinal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class Curried(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String) {
  def dump = {
    mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "curried", "()Lscala/Function1;", "()Lscala/Function1<Ljava/lang/String;Lscala/Function1<Ljava/lang/Object;Lscala/Function1<Ljava/lang/Object;L" + caseClassName + ";>;>;>;", null);
    mv.visitCode();
    mv.visitFieldInsn(GETSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
    mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "curried", "()Lscala/Function1;");
    mv.visitInsn(ARETURN);
    mv.visitMaxs(1, 0);
    mv.visitEnd();
  }
}
