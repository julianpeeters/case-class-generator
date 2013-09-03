package models
//import java.util._
import org.objectweb.asm._
import Opcodes._
//import org.objectweb.asm.attrs.*;
/*
public class MyRecordDump implements Opcodes {

public static List<byte[]> dump () throws Exception {
*/
case class ClassData(
  classNamespace: String, 
  className: String, 
  classFields: List[FieldData], 
  returnType: List[Any] )



case class FieldData(
  fieldName: String, 
  fieldType: String, 
  typeDescriptor: String,
  unapplyType: String,
  loadInstr: Int, 
  returnInstr: Int)

class MyRecordDump {

  //def dump(classData: ClassData): List[Array[Byte]] = {
  def dump: List[Array[Byte]] = {
 // def dump: Array[Byte] = {

    val caseClassName = "models/MyRecord"//caseClassName
    val className = "MyRecord"
    val classNamespace = "models"
    val name = className
   // val fieldData: List[FieldData] = classData.classFields
    val fieldData: List[FieldData] = List(FieldData("x","string","Ljava/lang/String;", "Ljava/lang/String;",25,176), FieldData("y","int","I", "Ljava/lang/Object;",21,172), FieldData("z","boolean","Z", "Ljava/lang/Object;",21,172))//classData.classFields
    val ctorReturnType = "(" + fieldData.map(n => n.typeDescriptor ).mkString + ")V"



//public static byte[] dump () throws Exception {
/*
ClassWriter cw = new ClassWriter(0);
FieldVisitor fv;
MethodVisitor mv;
AnnotationVisitor av0;
*/
    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);//|ClassWriter.COMPUTE_FRAMES); //now visit max's args don't matter
    var fv: FieldVisitor = null
    var mv: MethodVisitor = null
    var av0: AnnotationVisitor = null

cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, caseClassName, null, "java/lang/Object", Array[String] ( "scala/Product", "scala/Serializable" ));

{
av0 = cw.visitAnnotation("Lscala/reflect/ScalaSignature;", true);
av0.visit("bytes", "\u0006\u0001\u0005\rc\u0001B\u0001\u0003\u0001\u0016\u0011\u0001\"T=SK\u000e|'\u000f\u001a\u0006\u0002\u0007\u00051Qn\u001c3fYN\u001c\u0001a\u0005\u0003\u0001\r1y\u0001CA\u0004\u000b\u001b\u0005A!\"A\u0005\u0002\u000bM\u001c\u0017\r\\1\n\u0005-A!AB!osJ+g\r\u0005\u0002\u0008\u001b%\u0011a\u0002\u0003\u0002\u0008!J|G-^2u!\u00099\u0001#\u0003\u0002\u0012\u0011\u0009a1+\u001a:jC2L'0\u00192mK\"A1\u0003\u0001BK\u0002\u0013\u0005A#A\u0001y+\u0005)\u0002C\u0001\u000c\u001a\u001d\u00099q#\u0003\u0002\u0019\u0011\u00051\u0001K]3eK\u001aL!AG\u000e\u0003\rM#(/\u001b8h\u0015\u0009A\u0002\u0002\u0003\u0005\u001e\u0001\u0009E\u0009\u0015!\u0003\u0016\u0003\u0009A\u0008\u0005\u0003\u0005 \u0001\u0009U\r\u0011\"\u0001!\u0003\u0005IX#A\u0011\u0011\u0005\u001d\u0011\u0013BA\u0012\u0009\u0005\rIe\u000e\u001e\u0005\u0009K\u0001\u0011\u0009\u0012)A\u0005C\u0005\u0011\u0011\u0010\u0009\u0005\u0009O\u0001\u0011)\u001a!C\u0001Q\u0005\u0009!0F\u0001*!\u00099!&\u0003\u0002,\u0011\u00099!i\\8mK\u0006t\u0007\u0002C\u0017\u0001\u0005#\u0005\u000b\u0011B\u0015\u0002\u0005i\u0004\u0003\"B\u0018\u0001\u0009\u0003\u0001\u0014A\u0002\u001fj]&$h\u0008\u0006\u00032gQ*\u0004C\u0001\u001a\u0001\u001b\u0005\u0011\u0001\"B\n/\u0001\u0004)\u0002\"B\u0010/\u0001\u0004\u0009\u0003\"B\u0014/\u0001\u0004I\u0003bB\u001c\u0001\u0003\u0003%\u0009\u0001O\u0001\u0005G>\u0004\u0018\u0010\u0006\u00032siZ\u0004bB\n7!\u0003\u0005\r!\u0006\u0005\u0008?Y\u0002\n\u00111\u0001\"\u0011\u001d9c\u0007%AA\u0002%Bq!\u0010\u0001\u0012\u0002\u0013\u0005a(\u0001\u0008d_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0003}R#!\u0006!,\u0003\u0005\u0003\"AQ$\u000e\u0003\rS!\u0001R#\u0002\u0013Ut7\r[3dW\u0016$'B\u0001$\u0009\u0003)\u0009gN\\8uCRLwN\\\u0005\u0003\u0011\u000e\u0013\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001dQ\u0005!%A\u0005\u0002-\u000babY8qs\u0012\"WMZ1vYR$#'F\u0001MU\u0009\u0009\u0003\u0009C\u0004O\u0001E\u0005I\u0011A(\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\u000cW\u000f\u001c;%gU\u0009\u0001K\u000b\u0002*\u0001\"9!\u000bAA\u0001\n\u0003\u001a\u0016!\u00049s_\u0012,8\r\u001e)sK\u001aL\u00070F\u0001U!\u0009)&,D\u0001W\u0015\u00099\u0006,\u0001\u0003mC:<'\"A-\u0002\u0009)\u000cg/Y\u0005\u00035YCq\u0001\u0018\u0001\u0002\u0002\u0013\u0005\u0001%\u0001\u0007qe>$Wo\u0019;Be&$\u0018\u0010C\u0004_\u0001\u0005\u0005I\u0011A0\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0011\u0001m\u0019\u0009\u0003\u000f\u0005L!A\u0019\u0005\u0003\u0007\u0005s\u0017\u0010C\u0004e;\u0006\u0005\u0009\u0019A\u0011\u0002\u0007a$\u0013\u0007C\u0004g\u0001\u0005\u0005I\u0011I4\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012\u0001\u001b\u0009\u0004S2\u0004W\"\u00016\u000b\u0005-D\u0011AC2pY2,7\r^5p]&\u0011QN\u001b\u0002\u0009\u0013R,'/\u0019;pe\"9q\u000eAA\u0001\n\u0003\u0001\u0018\u0001C2b]\u0016\u000bX/\u00197\u0015\u0005%\n\u0008b\u00023o\u0003\u0003\u0005\r\u0001\u0019\u0005\u0008g\u0002\u0009\u0009\u0011\"\u0011u\u0003!A\u0017m\u001d5D_\u0012,G#A\u0011\u0009\u000fY\u0004\u0011\u0011!C!o\u0006AAo\\*ue&tw\rF\u0001U\u0011\u001dI\u0008!!A\u0005Bi\u000ca!Z9vC2\u001cHCA\u0015|\u0011\u001d!\u00070!AA\u0002\u0001<q! \u0002\u0002\u0002#\u0005a0\u0001\u0005NsJ+7m\u001c:e!\u0009\u0011tP\u0002\u0005\u0002\u0005\u0005\u0005\u0009\u0012AA\u0001'\u0011y\u00181A\u0008\u0011\u0011\u0005\u0015\u00111B\u000b\"SEj!!a\u0002\u000b\u0007\u0005%\u0001\"A\u0004sk:$\u0018.\\3\n\u0009\u00055\u0011q\u0001\u0002\u0012\u0003\n\u001cHO]1di\u001a+hn\u0019;j_:\u001c\u0004BB\u0018\u0000\u0009\u0003\u0009\u0009\u0002F\u0001\u0011\u001d1x0!A\u0005F]D\u0011\"a\u0006\u0000\u0003\u0003%\u0009)!\u0007\u0002\u000b\u0005\u0004\u0008\u000f\\=\u0015\u000fE\nY\"!\u0008\u0002 !11#!\u0006A\u0002UAaaHA\u000b\u0001\u0004\u0009\u0003BB\u0014\u0002\u0016\u0001\u0007\u0011\u0006C\u0005\u0002$}\u000c\u0009\u0011\"!\u0002&\u00059QO\\1qa2LH\u0003BA\u0014\u0003g\u0001RaBA\u0015\u0003[I1!a\u000b\u0009\u0005\u0019y\u0005\u000f^5p]B1q!a\u000c\u0016C%J1!!\r\u0009\u0005\u0019!V\u000f\u001d7fg!I\u0011QGA\u0011\u0003\u0003\u0005\r!M\u0001\u0004q\u0012\u0002\u0004\"CA\u001d\u0006\u0005I\u0011BA\u001e\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005u\u0002cA+\u0002@%\u0019\u0011\u0011\u0009,\u0003\r=\u0013'.Z2u\u0001");
av0.visitEnd();
}
// ATTRIBUTE ScalaSig
/*
{
fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "x", "Ljava/lang/String;", null, null);
fv.visitEnd();
}
{
fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "y", "I", null, null);
fv.visitEnd();
}
{
fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "z", "Z", null, null);
fv.visitEnd();
}
*/
fieldData.foreach(n=>(cw.visitField(ACC_PRIVATE + ACC_FINAL, n.fieldName, n.typeDescriptor.toString, null, null).visitEnd())) 




if (fieldData.length >= 1) {

mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "tupled", "()Lscala/Function1;", "()Lscala/Function1<Lscala/Tuple" + fieldData.length + "<" + fieldData.map(fd => fd.unapplyType).mkString + ">;L" + caseClassName + ";>;", null);

mv.visitCode();
mv.visitFieldInsn(GETSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "tupled", "()Lscala/Function1;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 0);
mv.visitEnd();


mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "curried", "()Lscala/Function1;", "()Lscala/Function1<Ljava/lang/String;Lscala/Function1<Ljava/lang/Object;Lscala/Function1<Ljava/lang/Object;L" + caseClassName + ";>;>;>;", null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "curried", "()Lscala/Function1;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 0);
mv.visitEnd();

}
/*
{
mv = cw.visitMethod(ACC_PUBLIC, "x", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, caseClassName, "x", "Ljava/lang/String;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "y", "()I", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, caseClassName, "y", "I");
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "z", "()Z", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, caseClassName, "z", "Z");
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
*/
fieldData.foreach(t => {
  mv = cw.visitMethod(ACC_PUBLIC, t.fieldName, "()"+t.typeDescriptor, null, null);
  mv.visitCode();
  mv.visitVarInsn(ALOAD, 0);
  mv.visitFieldInsn(GETFIELD, caseClassName, t.fieldName, t.typeDescriptor.toString);
  mv.visitInsn(t.returnInstr);
  mv.visitMaxs(1, 1);
  mv.visitEnd();})




mv = cw.visitMethod(ACC_PUBLIC, "copy", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")L" +  caseClassName + ";", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, caseClassName);
mv.visitInsn(DUP);
/*
mv.visitVarInsn(ALOAD, 1);
mv.visitVarInsn(ILOAD, 2);
mv.visitVarInsn(ILOAD, 3);
*/
fieldData.zipWithIndex.foreach(fd => mv.visitVarInsn(fd._1.loadInstr, fd._2 + 1) )
mv.visitMethodInsn(INVOKESPECIAL, caseClassName, "<init>", "(" +fieldData.map(fd => fd.typeDescriptor).mkString + ")V");
mv.visitInsn(ARETURN);
mv.visitMaxs(5, 4);
mv.visitEnd();


/*
{
mv = cw.visitMethod(ACC_PUBLIC, "copy$default$1", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "x", "()Ljava/lang/String;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "copy$default$2", "()I", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "y", "()I");
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "copy$default$3", "()Z", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "z", "()Z");
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
*/
fieldData.zipWithIndex.foreach(fd => {
mv = cw.visitMethod(ACC_PUBLIC, "copy$default$" + fd._2, "()"  + fd._1.typeDescriptor, null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd._1.fieldName, "()"  + fd._1.typeDescriptor);
mv.visitInsn(fd._1.returnInstr);
mv.visitMaxs(1, 1);
mv.visitEnd();
})

//////////////////////////////////////////////////////////////////////

mv = cw.visitMethod(ACC_PUBLIC, "productPrefix", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitLdcInsn(className);
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
/*

{
mv = cw.visitMethod(ACC_PUBLIC, "productArity", "()I", null, null);
mv.visitCode();
mv.visitInsn(ICONST_3);
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
*/
//val ICONST_VALUE = debug.Main.typedFields.length match {
val ICONST_VALUE = fieldData.length match {
  case 1 => ICONST_1
  case 2 => ICONST_2
  case 3 => ICONST_3
  case 4 => ICONST_4
/* why are these not allowed?
  case 5 => ICONST_5
  case 6 => ICONST_6
  case 7 => ICONST_7
  case 8 => ICONST_8
  case 9 => ICONST_9
  case 10 => ICONST_10
  case 11 => ICONST_11
  case 12 => ICONST_12
  case 13 => ICONST_13
  case 14 => ICONST_14
  case 15 => ICONST_15
  case 16 => ICONST_16
  case 17 => ICONST_17
  case 18 => ICONST_18
  case 19 => ICONST_19
  case 20 => ICONST_20
  case 21 => ICONST_21
  case 22 => ICONST_22
//thru 22, the limit of a case class
*/
  case _ => "not a valid number of value members"      
}

mv = cw.visitMethod(ACC_PUBLIC, "productArity", "()I", null, null);
mv.visitCode();
//mv.visitInsn(ICONST_3);
mv.visitInsn(ICONST_VALUE.toString.toInt);//TODO best way to get the correct type? or better to do it in the match?
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();


/*
{
mv = cw.visitMethod(ACC_PUBLIC, "productElement", "(I)Ljava/lang/Object;", null, null);
mv.visitCode();
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ISTORE, 2);
mv.visitVarInsn(ILOAD, 2);
val l0 = new Label();
val l1 = new Label();
val l2 = new Label();
val l3 = new Label();
val params = Array[Label](l0, l1, l2)
mv.visitTableSwitchInsn(0, 2, l3, params:_*);
mv.visitLabel(l3);
mv.visitFrame(Opcodes.F_APPEND,1, Array[Object] (Opcodes.INTEGER), 0, null);
mv.visitTypeInsn(NEW, "java/lang/IndexOutOfBoundsException");
mv.visitInsn(DUP);
mv.visitVarInsn(ILOAD, 1);
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IndexOutOfBoundsException", "<init>", "(Ljava/lang/String;)V");
mv.visitInsn(ATHROW);
mv.visitLabel(l2);
mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "z", "()Z");
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
val l4 = new Label();
mv.visitJumpInsn(GOTO, l4);
mv.visitLabel(l1);
mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "y", "()I");
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv.visitJumpInsn(GOTO, l4);
mv.visitLabel(l0);
mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "x", "()Ljava/lang/String;");
mv.visitLabel(l4);
mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] ("java/lang/Object"));
mv.visitInsn(ARETURN);
mv.visitMaxs(3, 3);
mv.visitEnd();
}
*/
{
mv = cw.visitMethod(ACC_PUBLIC, "productElement", "(I)Ljava/lang/Object;", null, null);
mv.visitCode();
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ISTORE, 2);
mv.visitVarInsn(ILOAD, 2);
/*
val l0 = new Label();
val l1 = new Label();
val l2 = new Label();
val l3 = new Label();
*/

val labels = (0 to fieldData.length).map(l => new Label())
//val labels = List(l0, l1, l2, l3)


//val params = Array[Label](l0, l1, l2)
val params = labels.take(3)
mv.visitTableSwitchInsn(0, labels.length - 2, labels(3), params:_*);
mv.visitLabel(labels(3));
mv.visitFrame(Opcodes.F_APPEND,1, Array[Object] (Opcodes.INTEGER), 0, null);
mv.visitTypeInsn(NEW, "java/lang/IndexOutOfBoundsException");
mv.visitInsn(DUP);
mv.visitVarInsn(ILOAD, 1);
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IndexOutOfBoundsException", "<init>", "(Ljava/lang/String;)V");
mv.visitInsn(ATHROW);
mv.visitLabel(labels(2));
mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

val reversed = fieldData.reverse
var terminalLabel: Label = null
reversed.take(3).foreach( valueMember => {

  mv.visitVarInsn(ALOAD, 0);
  mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
  valueMember.fieldType match { 
    case "byte" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToByte", "(B)Ljava/lang/Byte;");
    case "short" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToShort", "(S)Ljava/lang/Short;");
    case "int" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
    case "long" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToLong", "(J)Ljava/lang/Long;");
    case "float" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToFloat", "(F)Ljava/lang/Float;");
    case "double" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToDouble", "(D)Ljava/lang/Double;");
    case "char" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToCharacter", "(C)Ljava/lang/Character;");
    case "string" => 
    case "boolean" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
    case "unit" => mv.visitFieldInsn(GETSTATIC, "scala/runtime/BoxedUnit", "UNIT", "Lscala/runtime/BoxedUnit;");
    case "null" => mv.visitInsn(POP); mv.visitInsn(ACONST_NULL);
    case "nothing" => mv.visitInsn(ATHROW);
    case "any" => 
    case "anyref" => 
    case "object" => 

    case "list" => 
    case _ => println("unsupported type")
  }
  if (fieldData.length > 1) {

    reversed.indexOf(valueMember) match {
      //The last field in the class
      case 0 => {
        terminalLabel = new Label()
        mv.visitJumpInsn(GOTO, terminalLabel);
        mv.visitLabel(labels(fieldData.indexOf(valueMember) - 1))
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      }
      //The first field, the last one written as bytecode
      case f if f == fieldData.length - 1 => { 
        mv.visitLabel(terminalLabel)
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] ("java/lang/Object"))
      }
      //The middle fields in the class
      case _ => {
        mv.visitJumpInsn(GOTO, terminalLabel);
        mv.visitLabel(labels(fieldData.indexOf(valueMember) - 1))
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null)
      }
    }
  }
})

mv.visitInsn(ARETURN);
mv.visitMaxs(3, 3);
mv.visitEnd();
}
/*
/*
  valueMember.fieldType match { 
    case "byte" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToByte", "(B)Ljava/lang/Byte;");
    case "short" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToShort", "(S)Ljava/lang/Short;");
    case "int" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
    case "long" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToLong", "(J)Ljava/lang/Long;");
    case "float" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToFloat", "(F)Ljava/lang/Float;");
    case "double" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToDouble", "(D)Ljava/lang/Double;");
    case "char" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToCharacter", "(C)Ljava/lang/Character;");
    case "string" => 
    case "boolean" => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
    case "unit" => mv.visitFieldInsn(GETSTATIC, "scala/runtime/BoxedUnit", "UNIT", "Lscala/runtime/BoxedUnit;");
    case "null" => mv.visitInsn(POP); mv.visitInsn(ACONST_NULL);
    case "nothing" => mv.visitInsn(ATHROW);
    case "any" => 
    case "anyref" => 
    case "object" => 

    case "list" => 
    case _ => println("unsupported type")
  }
println(fieldData.indexOf(valueMember))
println(terminalLabel)
  if (fieldData.length > 1) {
//if it's the first one written (i.e. the last field), then make a last label
    if (fieldData.indexOf(valueMember) == fieldData.length - 1) terminalLabel = new Label(); println(terminalLabel)
    fieldData.reverse.indexOf(valueMember) match {
//The last field in the class
      case 0 => {println("last " + valueMember.fieldType + terminalLabel)
        mv.visitLabel(terminalLabel);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      }
//The first field, the last one written as bytecode
      case x if x == fieldData.length -1 => { println("first " + valueMember.fieldType + terminalLabel)
        mv.visitJumpInsn(GOTO, terminalLabel);
        mv.visitLabel(labels(labels.length - (fieldData.indexOf(valueMember) + 1) )); println(labels.length - (fieldData.reverse.indexOf(valueMember) + 1) )
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] ("java/lang/Object"));
      }
//The middle fields in the class
      case _ => { println( terminalLabel)
        mv.visitJumpInsn(GOTO, terminalLabel);
        mv.visitLabel(labels(labels.length - (fieldData.indexOf(valueMember) + 1) ));println(labels.length - (fieldData.reverse.indexOf(valueMember) + 1) )
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      }
    }
*/
*/

/*
mv = cw.visitMethod(ACC_PUBLIC, "productElement", "(I)Ljava/lang/Object;", null, null);
mv.visitCode();
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ISTORE, 2);
mv.visitVarInsn(ILOAD, 2);

//variable section

//TODO try making an a list of labels, won't have to name each one?
val labels = fieldData.map(t => (new Label()))
//println(labels)
//
val l0 = new Label();
val l1 = new Label();
val l2 = new Label();
val l3 = new Label();
val params = Array[Label](l0, l1, l2)
mv.visitTableSwitchInsn(0, 2, l3, params:_*);
mv.visitLabel(l3);
//end variable section 


mv.visitTypeInsn(NEW, "java/lang/IndexOutOfBoundsException");
mv.visitInsn(DUP);
mv.visitVarInsn(ILOAD, 1);
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IndexOutOfBoundsException", "<init>", "(Ljava/lang/String;)V");
mv.visitInsn(ATHROW);

//variable section
mv.visitLabel(l0);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "x", "()Ljava/lang/String;");
val l4 = new Label();
mv.visitJumpInsn(GOTO, l4);
mv.visitLabel(l2);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "z", "()Z");
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
mv.visitJumpInsn(GOTO, l4);
mv.visitLabel(l1);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "y", "()I");
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv.visitLabel(l4);
mv.visitInsn(ARETURN);
//end variable section


mv.visitMaxs(3, 3);
mv.visitEnd();
*/
///////////////////////////////////////////////////////////////////////////////////////


mv = cw.visitMethod(ACC_PUBLIC, "productIterator", "()Lscala/collection/Iterator;", "()Lscala/collection/Iterator<Ljava/lang/Object;>;", null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "typedProductIterator", "(Lscala/Product;)Lscala/collection/Iterator;");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 1);
mv.visitEnd();





mv = cw.visitMethod(ACC_PUBLIC, "canEqual", "(Ljava/lang/Object;)Z", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 1);
mv.visitTypeInsn(INSTANCEOF, caseClassName);
mv.visitInsn(IRETURN);
mv.visitMaxs(1, 2);
mv.visitEnd();


{
mv = cw.visitMethod(ACC_PUBLIC, "hashCode", "()I", null, null);
mv.visitCode();
mv.visitLdcInsn(new Integer(-889275714));
mv.visitVarInsn(ISTORE, 1);
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "x", "()Ljava/lang/String;");
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "anyHash", "(Ljava/lang/Object;)I");
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
mv.visitVarInsn(ISTORE, 1);
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "y", "()I");
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
mv.visitVarInsn(ISTORE, 1);
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "z", "()Z");
val l0 = new Label();
mv.visitJumpInsn(IFEQ, l0);
mv.visitIntInsn(SIPUSH, 1231);
val l1 = new Label();
mv.visitJumpInsn(GOTO, l1);
mv.visitLabel(l0);
mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, Opcodes.INTEGER), 1, Array[Object] (Opcodes.INTEGER));
mv.visitIntInsn(SIPUSH, 1237);
mv.visitLabel(l1);
mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, Opcodes.INTEGER), 2, Array[Object] (Opcodes.INTEGER, Opcodes.INTEGER));
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
mv.visitVarInsn(ISTORE, 1);
mv.visitVarInsn(ILOAD, 1);
mv.visitInsn(ICONST_3);
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "finalizeHash", "(II)I");
mv.visitInsn(IRETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}


mv = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_toString", "(Lscala/Product;)Ljava/lang/String;");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 1);
mv.visitEnd();






{
mv = cw.visitMethod(ACC_PUBLIC, "equals", "(Ljava/lang/Object;)Z", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
val l0 = new Label();
mv.visitJumpInsn(IF_ACMPEQ, l0);
mv.visitVarInsn(ALOAD, 1);
mv.visitVarInsn(ASTORE, 2);
mv.visitVarInsn(ALOAD, 2);
mv.visitTypeInsn(INSTANCEOF, caseClassName);
val l1 = new Label();
mv.visitJumpInsn(IFEQ, l1);
mv.visitInsn(ICONST_1);
mv.visitVarInsn(ISTORE, 3);
val l2 = new Label();
mv.visitJumpInsn(GOTO, l2);
mv.visitLabel(l1);
mv.visitFrame(Opcodes.F_APPEND,1, Array[Object] ("java/lang/Object"), 0, null);
mv.visitInsn(ICONST_0);
mv.visitVarInsn(ISTORE, 3);
mv.visitLabel(l2);
mv.visitFrame(Opcodes.F_APPEND,1, Array[Object] (Opcodes.INTEGER), 0, null);
mv.visitVarInsn(ILOAD, 3);
val l3 = new Label();
mv.visitJumpInsn(IFEQ, l3);
mv.visitVarInsn(ALOAD, 1);
mv.visitTypeInsn(CHECKCAST, caseClassName);
mv.visitVarInsn(ASTORE, 4);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "x", "()Ljava/lang/String;");
mv.visitVarInsn(ALOAD, 4);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "x", "()Ljava/lang/String;");
mv.visitVarInsn(ASTORE, 5);
mv.visitInsn(DUP);
val l4 = new Label();
mv.visitJumpInsn(IFNONNULL, l4);
mv.visitInsn(POP);
mv.visitVarInsn(ALOAD, 5);
val l5 = new Label();
mv.visitJumpInsn(IFNULL, l5);
val l6 = new Label();
mv.visitJumpInsn(GOTO, l6);
mv.visitLabel(l4);
mv.visitFrame(Opcodes.F_FULL, 6, Array[Object] (caseClassName, "java/lang/Object", "java/lang/Object", Opcodes.INTEGER, caseClassName, "java/lang/String"), 1, Array[Object] ("java/lang/String"));
mv.visitVarInsn(ALOAD, 5);
mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
mv.visitJumpInsn(IFEQ, l6);
mv.visitLabel(l5);
mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "y", "()I");
mv.visitVarInsn(ALOAD, 4);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "y", "()I");
mv.visitJumpInsn(IF_ICMPNE, l6);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "z", "()Z");
mv.visitVarInsn(ALOAD, 4);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "z", "()Z");
mv.visitJumpInsn(IF_ICMPNE, l6);
mv.visitVarInsn(ALOAD, 4);
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "canEqual", "(Ljava/lang/Object;)Z");
mv.visitJumpInsn(IFEQ, l6);
mv.visitInsn(ICONST_1);
val l7 = new Label();
mv.visitJumpInsn(GOTO, l7);
mv.visitLabel(l6);
mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
mv.visitInsn(ICONST_0);
mv.visitLabel(l7);
mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] {Opcodes.INTEGER});
mv.visitJumpInsn(IFEQ, l3);
mv.visitLabel(l0);
mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, "java/lang/Object"), 0, Array[Object] ());
mv.visitInsn(ICONST_1);
val l8 = new Label();
mv.visitJumpInsn(GOTO, l8);
mv.visitLabel(l3);
mv.visitFrame(Opcodes.F_APPEND,2, Array[Object] ("java/lang/Object", Opcodes.INTEGER), 0, null);
mv.visitInsn(ICONST_0);
mv.visitLabel(l8);
mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, "java/lang/Object"), 1, Array[Object] (Opcodes.INTEGER));
mv.visitInsn(IRETURN);
mv.visitMaxs(2, 6);
mv.visitEnd();
}

/*
{
mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;IZ)V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
mv.visitFieldInsn(PUTFIELD, caseClassName, "x", "Ljava/lang/String;");
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ILOAD, 2);
mv.visitFieldInsn(PUTFIELD, caseClassName, "y", "I");
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ILOAD, 3);
mv.visitFieldInsn(PUTFIELD, caseClassName, "z", "Z");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESTATIC, "scala/Product$class", "$init$", "(Lscala/Product;)V");
mv.visitInsn(RETURN);
mv.visitMaxs(2, 4);
mv.visitEnd();
}
*/
//init method
mv = cw.visitMethod(ACC_PUBLIC, "<init>", ctorReturnType, null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
//the variable part of the constructor:

if (fieldData.length == 0) mv.visitVarInsn(ALOAD, 0); //if the case class has no value members
else fieldData.foreach(t => {
  
//println(fieldData.indexOf(t))

  mv.visitVarInsn(t.loadInstr, (fieldData.indexOf(t))+1);
  mv.visitFieldInsn(PUTFIELD, caseClassName, t.fieldName, t.typeDescriptor.toString);
  mv.visitVarInsn(ALOAD, 0);
})
/*
if (debug.Main.typedFields.length == 0) mv.visitVarInsn(ALOAD, 0); //if the case class has no value members
else debug.Main.typedFields.foreach(t => {println(debug.Main.typedFields.indexOf(t))
  mv.visitVarInsn(t._5, (debug.Main.typedFields.indexOf(t))+1);
  mv.visitFieldInsn(PUTFIELD, caseClassName, t._1, t._3);
  mv.visitVarInsn(ALOAD, 0);
})
*/

mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESTATIC, "scala/Product$class", "$init$", "(Lscala/Product;)V");
mv.visitInsn(RETURN);
mv.visitMaxs(2, 4);
mv.visitEnd();





cw.visitEnd();

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

cw_MODULE.visit(V1_6, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, caseClassName + "$", "Lscala/runtime/AbstractFunction" + fieldData.length + "<" + fieldData.map(fd => fd.unapplyType).mkString + "L" + caseClassName + ";>;Lscala/Serializable;", "scala/runtime/AbstractFunction" + fieldData.length, Array[String] ("scala/Serializable" ));

// ATTRIBUTE Scala

fv_MODULE = cw_MODULE.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "MODULE$", "L" + caseClassName + "$;", null, null);
fv_MODULE.visitEnd();


mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_STATIC, "<clinit>", "()V", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitTypeInsn(NEW, caseClassName + "$");
mv_MODULE.visitMethodInsn(INVOKESPECIAL, caseClassName + "$", "<init>", "()V");
mv_MODULE.visitInsn(RETURN);
mv_MODULE.visitMaxs(1, 0);
mv_MODULE.visitEnd();


mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_FINAL, "toString", "()Ljava/lang/String;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitLdcInsn(className);
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();


mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "apply", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")L" + caseClassName + ";", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitTypeInsn(NEW, caseClassName);
mv_MODULE.visitInsn(DUP);
/*
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitVarInsn(ILOAD, 2);
mv_MODULE.visitVarInsn(ILOAD, 3);
*/
fieldData.map(fd => fd.loadInstr).zipWithIndex.foreach(lI => mv_MODULE.visitVarInsn(lI._1, (lI._2 + 1)))
mv_MODULE.visitMethodInsn(INVOKESPECIAL, caseClassName, "<init>", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")V");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(5, 4);
mv_MODULE.visitEnd();



  mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(L" + caseClassName + ";)Lscala/Option;", "(" + caseClassName + ";)Lscala/Option<Lscala/Tuple" + fieldData.length + "<" + fieldData.map(fd => fd.unapplyType).mkString + ">;>;", null);
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
mv_MODULE.visitTypeInsn(NEW, "scala/Tuple" + fieldData.length);
mv_MODULE.visitInsn(DUP);

fieldData.foreach(fd => {
  mv_MODULE.visitVarInsn(ALOAD, 1);
  mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeDescriptor);
  if (fd.fieldType == "int") mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
  if (fd.fieldType == "boolean") mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
})
/*
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "x", "()Ljava/lang/String;");
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "y", "()I");
mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "z", "()Z");
mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
*/
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/Tuple" + fieldData.length, "<init>", "(" + Stream.continually("Ljava/lang/Object;").take(fieldData.length).mkString + ")V");
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/Some", "<init>", "(Ljava/lang/Object;)V");
mv_MODULE.visitLabel(l1_MODULE);
mv_MODULE.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] ("scala/Option"));
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(7, 2);
mv_MODULE.visitEnd();


mv_MODULE = cw_MODULE.visitMethod(ACC_PRIVATE, "readResolve", "()Ljava/lang/Object;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitFieldInsn(GETSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();



mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "apply", "(" + Stream.continually("Ljava/lang/Object;").take(fieldData.length).mkString + ")Ljava/lang/Object;", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 0);
/*
mv_MODULE.visitVarInsn(ALOAD, 1);
mv_MODULE.visitTypeInsn(CHECKCAST, "java/lang/String");
mv_MODULE.visitVarInsn(ALOAD, 2);
mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToInt", "(Ljava/lang/Object;)I");
mv_MODULE.visitVarInsn(ALOAD, 3);
mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToBoolean", "(Ljava/lang/Object;)Z");
*/
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





//mv_MODULE = cw_MODULE.visitMethod(ACC_PRIVATE, "<init>", "()V", null, null);
mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 0);
//mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/runtime/AbstractFunction3", "<init>", "()V");
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/runtime/AbstractFunction" + fieldData.length, "<init>", "()V");
mv_MODULE.visitVarInsn(ALOAD, 0);
//mv_MODULE.visitFieldInsn(PUTSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
mv_MODULE.visitFieldInsn(PUTSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
mv_MODULE.visitInsn(RETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();

cw_MODULE.visitEnd();

//List<byte[]> dumps = Arrays.asList(cw.toByteArray(), cw_MODULE.toByteArray());
List(cw.toByteArray(), cw_MODULE.toByteArray());



//List<Byte[]> byteArrayList = new ArrayList<Byte[]>();
//byteArrayList.add(cw.toByteArray());
//byteArrayList.add(cw_MODULE.toByteArray());

//return dumps;
//return cw.toByteArray();
}
}
