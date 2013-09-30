package models
import avocet._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class FieldMethods(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    fieldData.foreach(t => {
      mv = cw.visitMethod(ACC_PUBLIC, t.fieldName, "()"+t.typeDescriptor, null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, caseClassName, t.fieldName, t.typeDescriptor.toString);
      mv.visitInsn(t.returnInstr);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    })
  }
}
