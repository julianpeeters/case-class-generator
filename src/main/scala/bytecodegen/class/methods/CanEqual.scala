package caseclass.generator
import artisinal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

case class CanEqual(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String) {
  def dump = {
    mv = cw.visitMethod(ACC_PUBLIC, "canEqual", "(Ljava/lang/Object;)Z", null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 1);
    mv.visitTypeInsn(INSTANCEOF, caseClassName);
    mv.visitInsn(IRETURN);
    mv.visitMaxs(1, 2);
    mv.visitEnd();
  }
}
