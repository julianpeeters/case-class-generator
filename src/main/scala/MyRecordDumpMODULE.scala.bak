package models
import java.util._
import org.objectweb.asm._
import Opcodes._
//import org.objectweb.asm.attrs.*;
/*
public class MyRecordDump implements Opcodes {

public static List<byte[]> dump () throws Exception {
*/


object MyRecordDumpMODULE {

  //def dump(classData: ClassData): List[Array[Byte]] = {
//  def dump: List[Array[Byte]] = {
  def dump: Array[Byte] = {



//return cw.toByteArray();
//}
/*
ClassWriter cw_MODULE = new ClassWriter(0);
FieldVisitor fv_MODULE;
MethodVisitor mv_MODULE;
AnnotationVisitor av0;
*/
val cw_MODULE: ClassWriter = new ClassWriter(0);
var fv_MODULE: FieldVisitor = null;
var mv_MODULE: MethodVisitor = null;
var av0_MODULE: AnnotationVisitor = null;

cw_MODULE.visit(V1_6, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, "models/MyRecord$", "Lscala/runtime/AbstractFunction3<Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Lmodels/MyRecord;>;Lscala/Serializable;", "scala/runtime/AbstractFunction3", Array[String] ("scala/Serializable" ));

// ATTRIBUTE Scala
{
fv_MODULE = cw_MODULE.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "MODULE$", "Lmodels/MyRecord$;", null, null);
fv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_STATIC, "<clinit>", "()V", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitTypeInsn(NEW, "models/MyRecord$");
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "models/MyRecord$", "<init>", "()V");
mv_MODULE.visitInsn(RETURN);
mv_MODULE.visitMaxs(1, 0);
mv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_FINAL, "toString", "()Ljava/lang/String;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitLdcInsn("MyRecord");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "apply", "(Ljava/lang/String;IZ)Lmodels/MyRecord;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitTypeInsn(NEW, "models/MyRecord");
mv_MODULE.visitInsn(DUP);
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitVarInsn(ILOAD, 2);
mv_MODULE.visitVarInsn(ILOAD, 3);
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "models/MyRecord", "<init>", "(Ljava/lang/String;IZ)V");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(5, 4);
mv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(Lmodels/MyRecord;)Lscala/Option;", "(Lmodels/MyRecord;)Lscala/Option<Lscala/Tuple3<Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;>;>;", null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 1);
val l0_MODULE = new Label();
mv_MODULE.visitJumpInsn(IFNONNULL, l0_MODULE);
mv_MODULE.visitFieldInsn(GETSTATIC, "scala/None$", "MODULE$", "Lscala/None$;");
val l1_MODULE = new Label();
mv_MODULE.visitJumpInsn(GOTO, l1_MODULE);
mv_MODULE.visitLabel(l0_MODULE);
mv_MODULE.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
mv_MODULE.visitTypeInsn(NEW, "scala/Some");
mv_MODULE.visitInsn(DUP);
mv_MODULE.visitTypeInsn(NEW, "scala/Tuple3");
mv_MODULE.visitInsn(DUP);
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord", "x", "()Ljava/lang/String;");
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord", "y", "()I");
mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord", "z", "()Z");
mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/Tuple3", "<init>", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V");
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/Some", "<init>", "(Ljava/lang/Object;)V");
mv_MODULE.visitLabel(l1_MODULE);
mv_MODULE.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] ("scala/Option"));
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(7, 2);
mv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PRIVATE, "readResolve", "()Ljava/lang/Object;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitFieldInsn(GETSTATIC, "models/MyRecord$", "MODULE$", "Lmodels/MyRecord$;");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "apply", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 0);
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitTypeInsn(CHECKCAST, "java/lang/String");
mv_MODULE.visitVarInsn(ALOAD, 2);
mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToInt", "(Ljava/lang/Object;)I");
mv_MODULE.visitVarInsn(ALOAD, 3);
mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToBoolean", "(Ljava/lang/Object;)Z");
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord$", "apply", "(Ljava/lang/String;IZ)Lmodels/MyRecord;");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(4, 4);
mv_MODULE.visitEnd();
}
{
//mv_MODULE = cw_MODULE.visitMethod(ACC_PRIVATE, "<init>", "()V", null, null);
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 0);
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/runtime/AbstractFunction3", "<init>", "()V");
mv_MODULE.visitVarInsn(ALOAD, 0);
mv_MODULE.visitFieldInsn(PUTSTATIC, "models/MyRecord$", "MODULE$", "Lmodels/MyRecord$;");
mv_MODULE.visitInsn(RETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();
}
cw_MODULE.visitEnd();

//List<byte[]> dumps = Arrays.asList(cw.toByteArray(), cw_MODULE.toByteArray());
//List(cw.toByteArray(), cw_MODULE.toByteArray());



//List<Byte[]> byteArrayList = new ArrayList<Byte[]>();
//byteArrayList.add(cw.toByteArray());
//byteArrayList.add(cw_MODULE.toByteArray());

//return dumps;
return cw_MODULE.toByteArray();
}
}
