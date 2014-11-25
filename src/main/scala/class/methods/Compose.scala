package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

case class Compose(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[EnrichedField]) {
  def dump = {

    mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "compose", "(Lscala/Function1;)Lscala/Function1;", "<A:Ljava/lang/Object;>(Lscala/Function1<TA;" + fieldData.map(fd => fd.typeData.unerasedType).head + ">;)Lscala/Function1<TA;L" + caseClassName + ";>;", null);

    mv.visitCode();
    mv.visitFieldInsn(GETSTATIC, "models/MyRecord$", "MODULE$", "Lmodels/MyRecord$;");
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord$", "andThen", "(Lscala/Function1;)Lscala/Function1;");
    mv.visitInsn(ARETURN);
    mv.visitMaxs(2, 1);
    mv.visitEnd();
  }
}
