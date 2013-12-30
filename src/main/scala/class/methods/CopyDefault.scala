package caseclass.generator
import org.objectweb.asm._
import Opcodes._

case class CopyDefault(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    fieldData.zipWithIndex.foreach(fd => {
      val tpe = {
        if (fd._1.typeData.typeDescriptor == "Lscala/runtime/BoxedUnit;") "V"
        else fd._1.typeData.typeDescriptor
      } 
      mv = cw.visitMethod(ACC_PUBLIC, "copy$default$" + fd._2, "()" + tpe, fd._1.typeData.unerasedType, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd._1.fieldName, "()"  + tpe);
      mv.visitInsn(fd._1.typeData.returnInstr);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    })
  }
}
