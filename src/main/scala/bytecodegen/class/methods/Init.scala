package models
import avocet._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

case class Init(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData], ctorReturnType: String) {
  def dump = {
    //init method
mv = cw.visitMethod(ACC_PUBLIC, "<init>", ctorReturnType, null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
//the variable part of the constructor:

if (fieldData.length == 0) mv.visitVarInsn(ALOAD, 0); //if the case class has no value members
else fieldData.foreach(t => {
  mv.visitVarInsn(t.loadInstr, (fieldData.indexOf(t))+1);
  mv.visitFieldInsn(PUTFIELD, caseClassName, t.fieldName, t.typeDescriptor.toString);
  mv.visitVarInsn(ALOAD, 0);
})

mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESTATIC, "scala/Product$class", "$init$", "(Lscala/Product;)V");
mv.visitInsn(RETURN);
mv.visitMaxs(2, 4);
mv.visitEnd();

cw.visitEnd();
  }
}
