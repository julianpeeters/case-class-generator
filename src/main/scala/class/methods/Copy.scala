package caseclass.generator
import artisinal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class Copy(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
  mv = cw.visitMethod(ACC_PUBLIC, "copy", "(" + fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ")L" +  caseClassName + ";", null, null);
    mv.visitCode();
    mv.visitTypeInsn(NEW, caseClassName);
    mv.visitInsn(DUP);

    var argIndex = 1

    fieldData.map(fd => fd.typeData.loadInstr).foreach(lI => {
      if (lI == DLOAD | lI == LLOAD ) {
        mv.visitVarInsn(lI, argIndex); 
        argIndex += 2
      }
      else { 
        mv.visitVarInsn(lI, argIndex); 
        argIndex += 1;
      }
    })

    mv.visitMethodInsn(INVOKESPECIAL, caseClassName, "<init>", "(" +fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ")V");
    mv.visitInsn(ARETURN);
    mv.visitMaxs(5, 4);
    mv.visitEnd();
  }
}
