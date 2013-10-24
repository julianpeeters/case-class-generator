package caseclass.generator
import org.objectweb.asm._
import Opcodes._

case class CopyDefault(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    fieldData.zipWithIndex.foreach(fd => {
      val tpe = {
        if (fd._1.typeDescriptor == "Lscala/runtime/BoxedUnit;") "V"
        else fd._1.typeDescriptor
      } 
     // mv = cw.visitMethod(ACC_PUBLIC, "copy$default$" + fd._2, "()"  + fd._1.typeDescriptor, null, null);
      mv = cw.visitMethod(ACC_PUBLIC, "copy$default$" + fd._2, "()" + tpe, null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd._1.fieldName, "()"  + tpe);
     // mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd._1.fieldName, "()"  + fd._1.typeDescriptor);
      mv.visitInsn(fd._1.returnInstr);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    })
  }
}
