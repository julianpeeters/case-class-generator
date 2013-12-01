package caseclass.generator
import artisinal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._

case class FieldMethods(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    fieldData.foreach(fd => {
      val tpe = {
        if (fd.typeData.typeDescriptor == "Lscala/runtime/BoxedUnit;") "V"
        else fd.typeData.typeDescriptor
      } 
      mv = cw.visitMethod(ACC_PUBLIC, fd.fieldName, "()"+tpe, null, null);
      mv.visitCode();
      if (fd.fieldType != "Unit") {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, caseClassName, fd.fieldName, fd.typeData.typeDescriptor.toString);
      }
      mv.visitInsn(fd.typeData.returnInstr);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    })
  }
}
