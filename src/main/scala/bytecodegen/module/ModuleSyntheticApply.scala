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
    case "int"     => {
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToInt", "(Ljava/lang/Object;)I");
    }
    case "boolean" => {
      mv_MODULE.visitVarInsn(ALOAD, n._2);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToBoolean", "(Ljava/lang/Object;)Z");
    }


    case "short"   => {
      mv_MODULE.visitVarInsn(ALOAD, n._2);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToShort", "(Ljava/lang/Object;)S");
    }
    case "long"    => {
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToLong", "(Ljava/lang/Object;)J");
    }
    case "float"   => {
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToFloat", "(Ljava/lang/Object;)F");
    }
    case "double"  =>  {
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToDouble", "(Ljava/lang/Object;)D");
    }
    case "byte"    => { 
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToByte", "(Ljava/lang/Object;)B");
    }
    case "char"    =>  {
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToChar", "(Ljava/lang/Object;)C");
    }
    case "unit"    => {
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/BoxedUnit");
    }
    case "null"    => {
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/Null$");
    }
    case "nothing" => {
      mv_MODULE.visitVarInsn(ALOAD, n._2); 
      mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/Nothing$");
    }
    case "any"     => mv_MODULE.visitVarInsn(ALOAD, n._2); 
    case "anyref"  => mv_MODULE.visitVarInsn(ALOAD, n._2); 
    case "object"  => mv_MODULE.visitVarInsn(ALOAD, n._2); 

    case "bytes"   => //TODO move this into avro datafile parser

    case _         => println("cannot generate apply method: unsupported type")
  }
})
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "apply", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")L" + caseClassName + ";");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(4, 4);
mv_MODULE.visitEnd();
  }
}
