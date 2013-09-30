package models
import avocet._
import org.objectweb.asm._
import Opcodes._


case class ModuleApply(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "apply", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")L" + caseClassName + ";", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitTypeInsn(NEW, caseClassName);
mv_MODULE.visitInsn(DUP);
fieldData.map(fd => fd.loadInstr).zipWithIndex.foreach(lI => mv_MODULE.visitVarInsn(lI._1, (lI._2 + 1)))
mv_MODULE.visitMethodInsn(INVOKESPECIAL, caseClassName, "<init>", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")V");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(5, 4);
mv_MODULE.visitEnd();
  }
}
