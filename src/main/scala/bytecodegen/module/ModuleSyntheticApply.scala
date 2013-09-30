package models
import avocet._
import org.objectweb.asm._
import Opcodes._


case class ModuleSyntheticApply(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    

mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "apply", "(" + Stream.continually("Ljava/lang/Object;").take(fieldData.length).mkString + ")Ljava/lang/Object;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 0);

fieldData.zipWithIndex.foreach(n => {

  n._1.fieldType match {
    case "string"  => mv_MODULE.visitVarInsn(ALOAD, n._2); mv_MODULE.visitTypeInsn(CHECKCAST, "java/lang/String");
    case "int"     => mv_MODULE.visitVarInsn(ALOAD, n._2); mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToInt", "(Ljava/lang/Object;)I");
    case "boolean" => mv_MODULE.visitVarInsn(ALOAD, n._2); mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToBoolean", "(Ljava/lang/Object;)Z");
  }
})
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "apply", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")L" + caseClassName + ";");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(4, 4);
mv_MODULE.visitEnd();
  }
}
