package models
import avocet._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class Copy(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
  mv = cw.visitMethod(ACC_PUBLIC, "copy", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")L" +  caseClassName + ";", null, null);
mv.visitCode();
println(fieldData(0).typeDescriptor)
mv.visitTypeInsn(NEW, caseClassName);
mv.visitInsn(DUP);
fieldData.zipWithIndex.foreach(fd => mv.visitVarInsn(fd._1.loadInstr, fd._2 + 1) )
mv.visitMethodInsn(INVOKESPECIAL, caseClassName, "<init>", "(" +fieldData.map(fd => fd.typeDescriptor).mkString + ")V");
mv.visitInsn(ARETURN);
mv.visitMaxs(5, 4);
mv.visitEnd();
  }
}
