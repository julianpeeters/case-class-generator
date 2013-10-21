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
      val tpe = {
        if (t.typeDescriptor == "Lscala/runtime/BoxedUnit;") "V"
        else t.typeDescriptor
      } 
    //  mv = cw.visitMethod(ACC_PUBLIC, t.fieldName, "()"+t.typeDescriptor, null, null);
      mv = cw.visitMethod(ACC_PUBLIC, t.fieldName, "()"+tpe, null, null);
      mv.visitCode();
      if (t.fieldType != "Unit") {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, caseClassName, t.fieldName, t.typeDescriptor.toString);
      }
      mv.visitInsn(t.returnInstr);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
    })
  }
}
