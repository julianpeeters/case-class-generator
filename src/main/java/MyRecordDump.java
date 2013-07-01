package models;
import java.util.*;
import org.objectweb.asm.*;
//import org.objectweb.asm.attrs.*;
public class MyRecordDump implements Opcodes {

public static List<byte[]> dump () throws Exception {

//public static byte[] dump () throws Exception {


ClassWriter cw = new ClassWriter(0);
FieldVisitor fv;
MethodVisitor mv;
AnnotationVisitor av0;

cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, "models/MyRecord", null, "java/lang/Object", new String[] { "scala/ScalaObject", "scala/Product", "scala/Serializable" });

{
av0 = cw.visitAnnotation("Lscala/reflect/ScalaSignature;", true);
av0.visit("bytes", "\u0006\u0001}4A!\u0001\u0002A\u000b\u0009AQ*\u001f*fG>\u0014HMC\u0001\u0004\u0003\u0019iw\u000eZ3mg\u000e\u00011#\u0002\u0001\u0007\u001dQ9\u0002CA\u0004\r\u001b\u0005A!BA\u0005\u000b\u0003\u0011a\u0017M\\4\u000b\u0003-\u0009AA[1wC&\u0011Q\u0002\u0003\u0002\u0007\u001f\nTWm\u0019;\u0011\u0005=\u0011R\"\u0001\u0009\u000b\u0003E\u0009Qa]2bY\u0006L!a\u0005\u0009\u0003\u0017M\u001b\u0017\r\\1PE*,7\r\u001e\u0009\u0003\u001fUI!A\u0006\u0009\u0003\u000fA\u0013x\u000eZ;diB\u0011q\u0002G\u0005\u00033A\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001b\u0007\u0001\u0003\u0016\u0004%\u0009\u0001H\u0001\u0002qV\u0009Q\u0004\u0005\u0002\u001fC9\u0011qbH\u0005\u0003AA\u0009a\u0001\u0015:fI\u00164\u0017B\u0001\u0012$\u0005\u0019\u0019FO]5oO*\u0011\u0001\u0005\u0005\u0005\u0009K\u0001\u0011\u0009\u0012)A\u0005;\u0005\u0011\u0001\u0010\u0009\u0005\u0006O\u0001!\u0009\u0001K\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0005%Z\u0003C\u0001\u0016\u0001\u001b\u0005\u0011\u0001\"B\u000e'\u0001\u0004i\u0002bB\u0017\u0001\u0003\u0003%\u0009AL\u0001\u0005G>\u0004\u0018\u0010\u0006\u0002*_!91\u0004\u000cI\u0001\u0002\u0004i\u0002bB\u0019\u0001#\u0003%\u0009AM\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00132+\u0005\u0019$FA\u000f5W\u0005)\u0004C\u0001\u001c<\u001b\u00059$B\u0001\u001d:\u0003%)hn\u00195fG.,GM\u0003\u0002;!\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\u0005q:$!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\")a\u0008\u0001C!\u0005A\u0001.Y:i\u0007>$W\rF\u0001A!\u0009y\u0011)\u0003\u0002C!\u0009\u0019\u0011J\u001c;\u0009\u000b\u0011\u0003A\u0011I#\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012!\u0008\u0005\u0006\u000f\u0002!\u0009\u0005S\u0001\u0007KF,\u0018\r\\:\u0015\u0005%c\u0005CA\u0008K\u0013\u0009Y\u0005CA\u0004C_>dW-\u00198\u0009\u000f53\u0015\u0011!a\u0001\u001d\u0006\u0019\u0001\u0010J\u0019\u0011\u0005=y\u0015B\u0001)\u0011\u0005\r\u0009e.\u001f\u0005\u0006%\u0002!\u0009eU\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003Q\u0003\"aB+\n\u0005\u0009B\u0001\"B,\u0001\u0009\u0003B\u0016\u0001\u00049s_\u0012,8\r^!sSRLX#\u0001!\u0009\u000bi\u0003A\u0011I.\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0011a\n\u0018\u0005\u0008\u001bf\u000b\u0009\u00111\u0001A\u0011\u0015q\u0006\u0001\"\u0011`\u0003!\u0019\u0017M\\#rk\u0006dGCA%a\u0011\u001diU,!AA\u00029;qA\u0019\u0002\u0002\u0002#\u00151-\u0001\u0005NsJ+7m\u001c:e!\u0009QCMB\u0004\u0002\u0005\u0005\u0005\u0009RA3\u0014\u0009\u00114gb\u0006\u0009\u0005O*l\u0012&D\u0001i\u0015\u0009I\u0007#A\u0004sk:$\u0018.\\3\n\u0005-D'!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8oc!)q\u0005\u001aC\u0001[R\u00091\rC\u0003EI\u0012\u0015s\u000eF\u0001U\u0011\u001d\u0009H-!A\u0005\u0002J\u000cQ!\u00199qYf$\"!K:\u0009\u000bm\u0001\u0008\u0019A\u000f\u0009\u000fU$\u0017\u0011!CAm\u00069QO\\1qa2LHCA<{!\ry\u00010H\u0005\u0003sB\u0011aa\u00149uS>t\u0007\"B>u\u0001\u0004I\u0013a\u0001=%a!)Q\u0010\u001aC\u0009}\u0006Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\u00051\u0001");
av0.visitEnd();
}
// ATTRIBUTE ScalaSig
{
fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "x", "Ljava/lang/String;", null, null);
fv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "andThen", "(Lscala/Function1;)Lscala/Function1;", "<A:Ljava/lang/Object;>(Lscala/Function1<Lmodels/MyRecord;TA;>;)Lscala/Function1<Ljava/lang/String;TA;>;", null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, "models/MyRecord$", "MODULE$", "Lmodels/MyRecord$;");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord$", "andThen", "(Lscala/Function1;)Lscala/Function1;");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "compose", "(Lscala/Function1;)Lscala/Function1;", "<A:Ljava/lang/Object;>(Lscala/Function1<TA;Ljava/lang/String;>;)Lscala/Function1<TA;Lmodels/MyRecord;>;", null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, "models/MyRecord$", "MODULE$", "Lmodels/MyRecord$;");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord$", "compose", "(Lscala/Function1;)Lscala/Function1;");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE, "productIterator", "()Lscala/collection/Iterator;", "()Lscala/collection/Iterator<Ljava/lang/Object;>;", null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESTATIC, "scala/Product$class", "productIterator", "(Lscala/Product;)Lscala/collection/Iterator;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_DEPRECATED, "productElements", "()Lscala/collection/Iterator;", "()Lscala/collection/Iterator<Ljava/lang/Object;>;", null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESTATIC, "scala/Product$class", "productElements", "(Lscala/Product;)Lscala/collection/Iterator;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "x", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, "models/MyRecord", "x", "Ljava/lang/String;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "copy", "(Ljava/lang/String;)Lmodels/MyRecord;", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, "models/MyRecord");
mv.visitInsn(DUP);
mv.visitVarInsn(ALOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "models/MyRecord", "<init>", "(Ljava/lang/String;)V");
mv.visitInsn(ARETURN);
mv.visitMaxs(3, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "copy$default$1", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord", "x", "()Ljava/lang/String;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "hashCode", "()I", null, null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_hashCode", "(Lscala/Product;)I");
mv.visitInsn(IRETURN);
mv.visitMaxs(2, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_toString", "(Lscala/Product;)Ljava/lang/String;");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "equals", "(Ljava/lang/Object;)Z", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
Label l0 = new Label();
mv.visitJumpInsn(IF_ACMPEQ, l0);
mv.visitVarInsn(ALOAD, 1);
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitTypeInsn(INSTANCEOF, "models/MyRecord");
Label l1 = new Label();
mv.visitJumpInsn(IFEQ, l1);
mv.visitVarInsn(ALOAD, 2);
mv.visitTypeInsn(CHECKCAST, "models/MyRecord");
mv.visitVarInsn(ASTORE, 3);
mv.visitVarInsn(ALOAD, 3);
mv.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord", "x", "()Ljava/lang/String;");
mv.visitVarInsn(ASTORE, 4);
mv.visitVarInsn(ALOAD, 4);
mv.visitVarInsn(ASTORE, 5);
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 5);
mv.visitMethodInsn(INVOKESPECIAL, "models/MyRecord", "gd1$1", "(Ljava/lang/String;)Z");
Label l2 = new Label();
mv.visitJumpInsn(IFEQ, l2);
mv.visitVarInsn(ALOAD, 1);
mv.visitTypeInsn(CHECKCAST, "models/MyRecord");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEINTERFACE, "scala/Equals", "canEqual", "(Ljava/lang/Object;)Z");
Label l3 = new Label();
mv.visitJumpInsn(GOTO, l3);
mv.visitLabel(l2);
mv.visitInsn(ICONST_0);
mv.visitJumpInsn(GOTO, l3);
mv.visitLabel(l1);
mv.visitInsn(ICONST_0);
mv.visitLabel(l3);
Label l4 = new Label();
mv.visitJumpInsn(IFEQ, l4);
mv.visitLabel(l0);
mv.visitInsn(ICONST_1);
Label l5 = new Label();
mv.visitJumpInsn(GOTO, l5);
mv.visitLabel(l4);
mv.visitInsn(ICONST_0);
mv.visitLabel(l5);
mv.visitInsn(IRETURN);
mv.visitMaxs(2, 6);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "productPrefix", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitLdcInsn("MyRecord");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "productArity", "()I", null, null);
mv.visitCode();
mv.visitInsn(ICONST_1);
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "productElement", "(I)Ljava/lang/Object;", null, null);
mv.visitCode();
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ISTORE, 2);
mv.visitVarInsn(ILOAD, 2);
mv.visitInsn(ICONST_0);
Label l0 = new Label();
mv.visitJumpInsn(IF_ICMPNE, l0);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord", "x", "()Ljava/lang/String;");
mv.visitInsn(ARETURN);
mv.visitLabel(l0);
mv.visitTypeInsn(NEW, "java/lang/IndexOutOfBoundsException");
mv.visitInsn(DUP);
mv.visitVarInsn(ILOAD, 1);
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IndexOutOfBoundsException", "<init>", "(Ljava/lang/String;)V");
mv.visitInsn(ATHROW);
mv.visitMaxs(3, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "canEqual", "(Ljava/lang/Object;)Z", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 1);
mv.visitTypeInsn(INSTANCEOF, "models/MyRecord");
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PRIVATE + ACC_FINAL, "gd1$1", "(Ljava/lang/String;)Z", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 1);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord", "x", "()Ljava/lang/String;");
mv.visitVarInsn(ASTORE, 2);
mv.visitInsn(DUP);
Label l0 = new Label();
mv.visitJumpInsn(IFNONNULL, l0);
mv.visitInsn(POP);
mv.visitVarInsn(ALOAD, 2);
Label l1 = new Label();
mv.visitJumpInsn(IFNULL, l1);
Label l2 = new Label();
mv.visitJumpInsn(GOTO, l2);
mv.visitLabel(l0);
mv.visitVarInsn(ALOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
mv.visitJumpInsn(IFEQ, l2);
mv.visitLabel(l1);
mv.visitInsn(ICONST_1);
Label l3 = new Label();
mv.visitJumpInsn(GOTO, l3);
mv.visitLabel(l2);
mv.visitInsn(ICONST_0);
mv.visitLabel(l3);
mv.visitInsn(IRETURN);
mv.visitMaxs(2, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;)V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
mv.visitFieldInsn(PUTFIELD, "models/MyRecord", "x", "Ljava/lang/String;");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESTATIC, "scala/Product$class", "$init$", "(Lscala/Product;)V");
mv.visitInsn(RETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}
cw.visitEnd();

ClassWriter cw_MODULE = new ClassWriter(0);
FieldVisitor fv_MODULE;
MethodVisitor mv_MODULE;
AnnotationVisitor av0_MODULE;

cw_MODULE.visit(V1_5, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, "models/MyRecord$", null, "scala/runtime/AbstractFunction1", new String[] { "scala/ScalaObject", "scala/Serializable" });

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
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(Lmodels/MyRecord;)Lscala/Option;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 1);
Label l0 = new Label();
mv_MODULE.visitJumpInsn(IFNONNULL, l0);
mv_MODULE.visitFieldInsn(GETSTATIC, "scala/None$", "MODULE$", "Lscala/None$;");
Label l1 = new Label();
mv_MODULE.visitJumpInsn(GOTO, l1);
mv_MODULE.visitLabel(l0);
mv_MODULE.visitTypeInsn(NEW, "scala/Some");
mv_MODULE.visitInsn(DUP);
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord", "x", "()Ljava/lang/String;");
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/Some", "<init>", "(Ljava/lang/Object;)V");
mv_MODULE.visitLabel(l1);
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(3, 2);
mv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "apply", "(Ljava/lang/String;)Lmodels/MyRecord;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitTypeInsn(NEW, "models/MyRecord");
mv_MODULE.visitInsn(DUP);
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "models/MyRecord", "<init>", "(Ljava/lang/String;)V");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(3, 2);
mv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "readResolve", "()Ljava/lang/Object;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitFieldInsn(GETSTATIC, "models/MyRecord$", "MODULE$", "Lmodels/MyRecord$;");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();
}
{
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_BRIDGE, "apply", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 0);
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitTypeInsn(CHECKCAST, "java/lang/String");
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, "models/MyRecord$", "apply", "(Ljava/lang/String;)Lmodels/MyRecord;");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(2, 2);
mv_MODULE.visitEnd();
}
{
//mv_MODULE = cw_MODULE.visitMethod(ACC_PRIVATE, "<init>", "()V", null, null);
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 0);
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/runtime/AbstractFunction1", "<init>", "()V");
mv_MODULE.visitVarInsn(ALOAD, 0);
mv_MODULE.visitFieldInsn(PUTSTATIC, "models/MyRecord$", "MODULE$", "Lmodels/MyRecord$;");
mv_MODULE.visitInsn(RETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();
}
cw_MODULE.visitEnd();

List<byte[]> dumps = Arrays.asList(cw.toByteArray(), cw_MODULE.toByteArray());



//List<Byte[]> byteArrayList = new ArrayList<Byte[]>();
//byteArrayList.add(cw.toByteArray());
//byteArrayList.add(cw_MODULE.toByteArray());

return dumps;
//return cw.toByteArray();
}
}
