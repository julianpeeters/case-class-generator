package models
import avocet._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

case class ProductArity(cw: ClassWriter, var mv: MethodVisitor, fieldData: List[FieldData]) {
  def dump = {
    val ICONST_VALUE = fieldData.length match {
  case 1 => ICONST_1
  case 2 => ICONST_2
  case 3 => ICONST_3
  case 4 => ICONST_4
  case _ => "not a valid number of value members"      
}
mv = cw.visitMethod(ACC_PUBLIC, "productArity", "()I", null, null);
mv.visitCode();
mv.visitInsn(ICONST_VALUE.toString.toInt);
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
  }
}
