package caseclass.generator
import artisinal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


case class ModuleReadResolve(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String) {
  def dump = {
    
mv_MODULE = cw_MODULE.visitMethod(ACC_PRIVATE, "readResolve", "()Ljava/lang/Object;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitFieldInsn(GETSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();

  }
}
