package models
import avocet._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._



class MyRecordDump {

  def dump(classData: ClassData): List[Array[Byte]] = {

    val caseClassName = classData.classNamespace + "/" + classData.className
    val name = classData.className
    val fieldData: List[FieldData] = classData.classFields.map(field => FieldMatcher.enrichFieldData(field) )
    val ctorReturnType = "(" + fieldData.map(n => n.typeDescriptor ).mkString + ")V"


    def capitalize(s: String) = { s(0).toString.toUpperCase + s.substring(1, s.length).toLowerCase }
    val mySig = new ScalaSig(List("case class"), List(classData.classNamespace, classData.className), fieldData.map(f => (f.fieldName, f.fieldType.capitalize)))

    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);//|ClassWriter.COMPUTE_FRAMES); //now visit max's args don't matter
    var fv: FieldVisitor = null
    var mv: MethodVisitor = null
    var av0: AnnotationVisitor = null

//the sig for string, int, boolean
val sig = "\u0006\u0001\u0005\rc\u0001B\u0001\u0003\u0001\u0016\u0011\u0001\"T=SK\u000e|'\u000f\u001a\u0006\u0002\u0007\u00051Qn\u001c3fYN\u001c\u0001a\u0005\u0003\u0001\r1y\u0001CA\u0004\u000b\u001b\u0005A!\"A\u0005\u0002\u000bM\u001c\u0017\r\\1\n\u0005-A!AB!osJ+g\r\u0005\u0002\u0008\u001b%\u0011a\u0002\u0003\u0002\u0008!J|G-^2u!\u00099\u0001#\u0003\u0002\u0012\u0011\u0009a1+\u001a:jC2L'0\u00192mK\"A1\u0003\u0001BK\u0002\u0013\u0005A#A\u0001y+\u0005)\u0002C\u0001\u000c\u001a\u001d\u00099q#\u0003\u0002\u0019\u0011\u00051\u0001K]3eK\u001aL!AG\u000e\u0003\rM#(/\u001b8h\u0015\u0009A\u0002\u0002\u0003\u0005\u001e\u0001\u0009E\u0009\u0015!\u0003\u0016\u0003\u0009A\u0008\u0005\u0003\u0005 \u0001\u0009U\r\u0011\"\u0001!\u0003\u0005IX#A\u0011\u0011\u0005\u001d\u0011\u0013BA\u0012\u0009\u0005\rIe\u000e\u001e\u0005\u0009K\u0001\u0011\u0009\u0012)A\u0005C\u0005\u0011\u0011\u0010\u0009\u0005\u0009O\u0001\u0011)\u001a!C\u0001Q\u0005\u0009!0F\u0001*!\u00099!&\u0003\u0002,\u0011\u00099!i\\8mK\u0006t\u0007\u0002C\u0017\u0001\u0005#\u0005\u000b\u0011B\u0015\u0002\u0005i\u0004\u0003\"B\u0018\u0001\u0009\u0003\u0001\u0014A\u0002\u001fj]&$h\u0008\u0006\u00032gQ*\u0004C\u0001\u001a\u0001\u001b\u0005\u0011\u0001\"B\n/\u0001\u0004)\u0002\"B\u0010/\u0001\u0004\u0009\u0003\"B\u0014/\u0001\u0004I\u0003bB\u001c\u0001\u0003\u0003%\u0009\u0001O\u0001\u0005G>\u0004\u0018\u0010\u0006\u00032siZ\u0004bB\n7!\u0003\u0005\r!\u0006\u0005\u0008?Y\u0002\n\u00111\u0001\"\u0011\u001d9c\u0007%AA\u0002%Bq!\u0010\u0001\u0012\u0002\u0013\u0005a(\u0001\u0008d_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0003}R#!\u0006!,\u0003\u0005\u0003\"AQ$\u000e\u0003\rS!\u0001R#\u0002\u0013Ut7\r[3dW\u0016$'B\u0001$\u0009\u0003)\u0009gN\\8uCRLwN\\\u0005\u0003\u0011\u000e\u0013\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001dQ\u0005!%A\u0005\u0002-\u000babY8qs\u0012\"WMZ1vYR$#'F\u0001MU\u0009\u0009\u0003\u0009C\u0004O\u0001E\u0005I\u0011A(\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\u000cW\u000f\u001c;%gU\u0009\u0001K\u000b\u0002*\u0001\"9!\u000bAA\u0001\n\u0003\u001a\u0016!\u00049s_\u0012,8\r\u001e)sK\u001aL\u00070F\u0001U!\u0009)&,D\u0001W\u0015\u00099\u0006,\u0001\u0003mC:<'\"A-\u0002\u0009)\u000cg/Y\u0005\u00035YCq\u0001\u0018\u0001\u0002\u0002\u0013\u0005\u0001%\u0001\u0007qe>$Wo\u0019;Be&$\u0018\u0010C\u0004_\u0001\u0005\u0005I\u0011A0\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0011\u0001m\u0019\u0009\u0003\u000f\u0005L!A\u0019\u0005\u0003\u0007\u0005s\u0017\u0010C\u0004e;\u0006\u0005\u0009\u0019A\u0011\u0002\u0007a$\u0013\u0007C\u0004g\u0001\u0005\u0005I\u0011I4\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012\u0001\u001b\u0009\u0004S2\u0004W\"\u00016\u000b\u0005-D\u0011AC2pY2,7\r^5p]&\u0011QN\u001b\u0002\u0009\u0013R,'/\u0019;pe\"9q\u000eAA\u0001\n\u0003\u0001\u0018\u0001C2b]\u0016\u000bX/\u00197\u0015\u0005%\n\u0008b\u00023o\u0003\u0003\u0005\r\u0001\u0019\u0005\u0008g\u0002\u0009\u0009\u0011\"\u0011u\u0003!A\u0017m\u001d5D_\u0012,G#A\u0011\u0009\u000fY\u0004\u0011\u0011!C!o\u0006AAo\\*ue&tw\rF\u0001U\u0011\u001dI\u0008!!A\u0005Bi\u000ca!Z9vC2\u001cHCA\u0015|\u0011\u001d!\u00070!AA\u0002\u0001<q! \u0002\u0002\u0002#\u0005a0\u0001\u0005NsJ+7m\u001c:e!\u0009\u0011tP\u0002\u0005\u0002\u0005\u0005\u0005\u0009\u0012AA\u0001'\u0011y\u00181A\u0008\u0011\u0011\u0005\u0015\u00111B\u000b\"SEj!!a\u0002\u000b\u0007\u0005%\u0001\"A\u0004sk:$\u0018.\\3\n\u0009\u00055\u0011q\u0001\u0002\u0012\u0003\n\u001cHO]1di\u001a+hn\u0019;j_:\u001c\u0004BB\u0018\u0000\u0009\u0003\u0009\u0009\u0002F\u0001\u0011\u001d1x0!A\u0005F]D\u0011\"a\u0006\u0000\u0003\u0003%\u0009)!\u0007\u0002\u000b\u0005\u0004\u0008\u000f\\=\u0015\u000fE\nY\"!\u0008\u0002 !11#!\u0006A\u0002UAaaHA\u000b\u0001\u0004\u0009\u0003BB\u0014\u0002\u0016\u0001\u0007\u0011\u0006C\u0005\u0002$}\u000c\u0009\u0011\"!\u0002&\u00059QO\\1qa2LH\u0003BA\u0014\u0003g\u0001RaBA\u0015\u0003[I1!a\u000b\u0009\u0005\u0019y\u0005\u000f^5p]B1q!a\u000c\u0016C%J1!!\r\u0009\u0005\u0019!V\u000f\u001d7fg!I\u0011QGA\u0011\u0003\u0003\u0005\r!M\u0001\u0004q\u0012\u0002\u0004\"CA\u001d\u0006\u0005I\u0011BA\u001e\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005u\u0002cA+\u0002@%\u0019\u0011\u0011\u0009,\u0003\r=\u0013'.Z2u\u0001"

//String
val stringSig = "\u0006\u0001\u0005=a\u0001B\u0001\u0003\u0001\u0016\u0011\u0001\"T=SK\u000e|'\u000f\u001a\u0006\u0002\u0007\u00051Qn\u001c3fYN\u001c\u0001a\u0005\u0003\u0001\r1y\u0001CA\u0004\u000b\u001b\u0005A!\"A\u0005\u0002\u000bM\u001c\u0017\r\\1\n\u0005-A!AB!osJ+g\r\u0005\u0002\u0008\u001b%\u0011a\u0002\u0003\u0002\u0008!J|G-^2u!\u00099\u0001#\u0003\u0002\u0012\u0011\u0009a1+\u001a:jC2L'0\u00192mK\"A1\u0003\u0001BK\u0002\u0013\u0005A#A\u0001y+\u0005)\u0002C\u0001\u000c\u001a\u001d\u00099q#\u0003\u0002\u0019\u0011\u00051\u0001K]3eK\u001aL!AG\u000e\u0003\rM#(/\u001b8h\u0015\u0009A\u0002\u0002\u0003\u0005\u001e\u0001\u0009E\u0009\u0015!\u0003\u0016\u0003\u0009A\u0008\u0005C\u0003 \u0001\u0011\u0005\u0001%\u0001\u0004=S:LGO\u0010\u000b\u0003C\r\u0002\"A\u0009\u0001\u000e\u0003\u0009AQa\u0005\u0010A\u0002UAq!\n\u0001\u0002\u0002\u0013\u0005a%\u0001\u0003d_BLHCA\u0011(\u0011\u001d\u0019B\u0005%AA\u0002UAq!\u000b\u0001\u0012\u0002\u0013\u0005!&\u0001\u0008d_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0003-R#!\u0006\u0017,\u00035\u0002\"AL\u001a\u000e\u0003=R!\u0001M\u0019\u0002\u0013Ut7\r[3dW\u0016$'B\u0001\u001a\u0009\u0003)\u0009gN\\8uCRLwN\\\u0005\u0003i=\u0012\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001d1\u0004!!A\u0005B]\nQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#\u0001\u001d\u0011\u0005erT\"\u0001\u001e\u000b\u0005mb\u0014\u0001\u00027b]\u001eT\u0011!P\u0001\u0005U\u00064\u0018-\u0003\u0002\u001bu!9\u0001\u0009AA\u0001\n\u0003\u0009\u0015\u0001\u00049s_\u0012,8\r^!sSRLX#\u0001\"\u0011\u0005\u001d\u0019\u0015B\u0001#\u0009\u0005\rIe\u000e\u001e\u0005\u0008\r\u0002\u0009\u0009\u0011\"\u0001H\u00039\u0001(o\u001c3vGR,E.Z7f]R$\"\u0001S&\u0011\u0005\u001dI\u0015B\u0001&\u0009\u0005\r\u0009e.\u001f\u0005\u0008\u0019\u0016\u000b\u0009\u00111\u0001C\u0003\rAH%\r\u0005\u0008\u001d\u0002\u0009\u0009\u0011\"\u0011P\u0003=\u0001(o\u001c3vGRLE/\u001a:bi>\u0014X#\u0001)\u0011\u0007E#\u0006*D\u0001S\u0015\u0009\u0019\u0006\"\u0001\u0006d_2dWm\u0019;j_:L!!\u0016*\u0003\u0011%#XM]1u_JDqa\u0016\u0001\u0002\u0002\u0013\u0005\u0001,\u0001\u0005dC:,\u0015/^1m)\u0009IF\u000c\u0005\u0002\u00085&\u00111\u000c\u0003\u0002\u0008\u0005>|G.Z1o\u0011\u001dae+!AA\u0002!CqA\u0018\u0001\u0002\u0002\u0013\u0005s,\u0001\u0005iCND7i\u001c3f)\u0005\u0011\u0005bB1\u0001\u0003\u0003%\u0009EY\u0001\u0009i>\u001cFO]5oOR\u0009\u0001\u0008C\u0004e\u0001\u0005\u0005I\u0011I3\u0002\r\u0015\u000cX/\u00197t)\u0009If\rC\u0004MG\u0006\u0005\u0009\u0019\u0001%\u0008\u000f!\u0014\u0011\u0011!E\u0001S\u0006AQ*\u001f*fG>\u0014H\r\u0005\u0002#U\u001a9\u0011AAA\u0001\u0012\u0003Y7c\u00016m\u001fA!Q\u000e]\u000b\"\u001b\u0005q'BA8\u0009\u0003\u001d\u0011XO\u001c;j[\u0016L!!\u001d8\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t\u0017\u0007C\u0003 U\u0012\u00051\u000fF\u0001j\u0011\u001d\u0009'.!A\u0005F\u0009DqA\u001e6\u0002\u0002\u0013\u0005u/A\u0003baBd\u0017\u0010\u0006\u0002\"q\")1#\u001ea\u0001+!9!P[A\u0001\n\u0003[\u0018aB;oCB\u0004H.\u001f\u000b\u0003y~\u00042aB?\u0016\u0013\u0009q\u0008B\u0001\u0004PaRLwN\u001c\u0005\u0007\u0003\u0003I\u0008\u0019A\u0011\u0002\u0007a$\u0003\u0007C\u0005\u0002\u0006)\u000c\u0009\u0011\"\u0003\u0002\u0008\u0005Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\u0009\u0009I\u0001E\u0002:\u0003\u0017I1!!\u0004;\u0005\u0019y%M[3di\u0002"

cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, caseClassName, null, "java/lang/Object", Array[String] ( "scala/Product", "scala/Serializable" ));








val str = new String(ByteCodecs.encode(mySig.bytes))
println(str.toCharArray.length)

val strSigArray = Arrays.copyOf(str.getBytes("UTF-8"), (ByteCodecs.decode(str.getBytes("UTF-8")))-1)

val reStr = new String(ByteCodecs.encode(strSigArray))
println(str.length == strSigArray.length)

println("mySig Decoded length: " + strSigArray.length)
val sigArray = Arrays.copyOf(sig.getBytes("UTF-8"), (ByteCodecs.decode(sig.getBytes("UTF-8")))-1)
println("sig Decoded length: " + sigArray.length)


val mySigArray = (Arrays.copyOf(str.getBytes("UTF-8"), ByteCodecs.decode(str.getBytes)))

println("+______________++++++++++++++++++_______________________+++++++++++++++++++")



{
av0 = cw.visitAnnotation("Lscala/reflect/ScalaSignature;", true);
av0.visit("bytes", new String(ByteCodecs.encode(mySig.bytes).dropRight(1)));
//av0.visit("bytes", stringSig);
//av0.visit("bytes", sig);

av0.visitEnd();
}

fieldData.foreach(n=>(cw.visitField(ACC_PRIVATE + ACC_FINAL, n.fieldName, n.typeDescriptor.toString, null, null).visitEnd())) 

if (fieldData.length >= 1) {
  mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "tupled", "()Lscala/Function1;", "()Lscala/Function1<Lscala/Tuple" +  fieldData.length + "<" + fieldData.map(fd => fd.unapplyType).mkString + ">;L" + caseClassName + ";>;", null);
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

fieldData.foreach(t => {
  mv = cw.visitMethod(ACC_PUBLIC, t.fieldName, "()"+t.typeDescriptor, null, null);
  mv.visitCode();
  mv.visitVarInsn(ALOAD, 0);
  mv.visitFieldInsn(GETFIELD, caseClassName, t.fieldName, t.typeDescriptor.toString);
  mv.visitInsn(t.returnInstr);
  mv.visitMaxs(1, 1);
  mv.visitEnd();
})

mv = cw.visitMethod(ACC_PUBLIC, "copy", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")L" +  caseClassName + ";", null, null);
mv.visitCode();
mv.visitTypeInsn(NEW, caseClassName);
mv.visitInsn(DUP);
fieldData.zipWithIndex.foreach(fd => mv.visitVarInsn(fd._1.loadInstr, fd._2 + 1) )
mv.visitMethodInsn(INVOKESPECIAL, caseClassName, "<init>", "(" +fieldData.map(fd => fd.typeDescriptor).mkString + ")V");
mv.visitInsn(ARETURN);
mv.visitMaxs(5, 4);
mv.visitEnd();

fieldData.zipWithIndex.foreach(fd => {
  mv = cw.visitMethod(ACC_PUBLIC, "copy$default$" + fd._2, "()"  + fd._1.typeDescriptor, null, null);
  mv.visitCode();
  mv.visitVarInsn(ALOAD, 0);
  mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd._1.fieldName, "()"  + fd._1.typeDescriptor);
  mv.visitInsn(fd._1.returnInstr);
  mv.visitMaxs(1, 1);
  mv.visitEnd();
})


mv = cw.visitMethod(ACC_PUBLIC, "productPrefix", "()Ljava/lang/String;", null, null);
mv.visitCode();
//mv.visitLdcInsn(className);
mv.visitLdcInsn(classData.className);
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
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

mv = cw.visitMethod(ACC_PUBLIC, "productElement", "(I)Ljava/lang/Object;", null, null);
mv.visitCode();
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ISTORE, 2);
mv.visitVarInsn(ILOAD, 2);
val labels = (0 to fieldData.length).map(l => new Label())
val params = labels.take(fieldData.length)
mv.visitTableSwitchInsn(0, labels.length - 2, labels(fieldData.length), params:_*);
mv.visitLabel(labels(fieldData.length));
mv.visitFrame(Opcodes.F_APPEND,1, Array[Object] (Opcodes.INTEGER), 0, null);
mv.visitTypeInsn(NEW, "java/lang/IndexOutOfBoundsException");
mv.visitInsn(DUP);
mv.visitVarInsn(ILOAD, 1);
mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IndexOutOfBoundsException", "<init>", "(Ljava/lang/String;)V");
mv.visitInsn(ATHROW);
mv.visitLabel(labels(fieldData.length-1));
mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

val reversed = fieldData.reverse
var terminalLabel: Label = null
reversed.take(fieldData.length).foreach( valueMember => {
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
//TODO
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

mv = cw.visitMethod(ACC_PUBLIC, "hashCode", "()I", null, null);
mv.visitCode();
fieldData.length match {
  case 0 => {
    mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_hashCode", "(Lscala/Product;)I");
  }
  case x if x > 0 => {
    if (fieldData.map(n => n.fieldType).forall(t => List("nothing", "null", "any", "anyref", "object", "string", "list", "stream").contains(t))) {//if all the valueMembers are in this list (of "empty" types, look different when paired with "real")
      mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_hashCode", "(Lscala/Product;)I");
    } 
    else {  
    mv.visitLdcInsn(new Integer(-889275714));
    mv.visitVarInsn(ISTORE, 1);
    mv.visitVarInsn(ILOAD, 1);

    val fields = if (fieldData.map(n=>n.fieldType).contains("nothing")) fieldData.reverse.dropWhile(valueMember =>  valueMember.fieldType != "nothing").reverse; else fieldData
    //if there is more than one non-"empty" type(see the list above), drop all types after the first "nothing".
    fields.foreach( valueMember => { 
      valueMember.fieldType match { 
        case "byte"|"short"|"int"|"long"|"float"|"double"|"unit"|"null" => {
          valueMember.fieldType match {
            case "byte"|"short"|"int" => { 
              mv.visitVarInsn(ALOAD, 0);
              mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
            }
            case "long"|"float"|"double" => {
              mv.visitVarInsn(ALOAD, 0);
              mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
              mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", valueMember.fieldType + "Hash", "(" + valueMember.typeDescriptor + ")I");
            }
            case "unit"|"null" => {
              mv.visitInsn(ICONST_0);
              mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
              mv.visitVarInsn(ISTORE, 1);
              mv.visitVarInsn(ILOAD, 1);
            }
            case _ => println("""whoops, how'd that get here?""")
          }
        }
        case "boolean" => {
          mv.visitVarInsn(ALOAD, 0);
          mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
          val l0 = new Label();
          mv.visitJumpInsn(IFEQ, l0);
          mv.visitIntInsn(SIPUSH, 1231);
          val l1 = new Label();
          mv.visitJumpInsn(GOTO, l1);
          mv.visitLabel(l0);
          mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, Opcodes.INTEGER), 1, Array[Object] (Opcodes.INTEGER));
          mv.visitIntInsn(SIPUSH, 1237);
          mv.visitLabel(l1);
          mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, Opcodes.INTEGER), 2, Array[Object] (Opcodes.INTEGER,  Opcodes.INTEGER));
          mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
          mv.visitVarInsn(ISTORE, 1);
          mv.visitVarInsn(ILOAD, 1);
        }
        //if there was only one valueMember, the "if" statement would have taken care of things
        //so this has to have come after

        case "any"|"anyref"|"object"|"string"|"list"|"stream"=> {
          mv.visitVarInsn(ALOAD, 0);
          mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
          mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "anyHash", "(Ljava/lang/Object;)I");
        }

        case "nothing" => { //if "nothing" is a value member's type, it will be the last one in the list of value members
          mv.visitVarInsn(ALOAD, 0);
          mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()Lscala/runtime/Nothing$;");
          mv.visitInsn(ATHROW);
        }
        case _ => println("unsupported type")
      }
      //Booleans and Units get special treatment because their ASM lines have a "mix" already
      if (valueMember.fieldType != "boolean" && valueMember.fieldType != "unit" && valueMember.fieldType != "null") {  
        mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
        mv.visitVarInsn(ISTORE, 1);
        mv.visitVarInsn(ILOAD, 1);
      }
    })      
    fieldData.length match {
      case 1 => mv.visitInsn(ICONST_1);
      case 2 => mv.visitInsn(ICONST_2);
      case 3 => mv.visitInsn(ICONST_3);
      case 4 => mv.visitInsn(ICONST_4);
      case 5 => mv.visitInsn(ICONST_5); 
      case x if x > 5  => mv.visitIntInsn(BIPUSH, x);
    }
    mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "finalizeHash", "(II)I"); 
    }
    if (!fieldData.map(n => n.fieldType).contains("nothing")) mv.visitInsn(IRETURN);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
  }
}

mv = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
mv.visitCode();
mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_toString", "(Lscala/Product;)Ljava/lang/String;");
mv.visitInsn(ARETURN);
mv.visitMaxs(2, 1);
mv.visitEnd();

mv = cw.visitMethod(ACC_PUBLIC, "equals", "(Ljava/lang/Object;)Z", null, null);
mv.visitCode();//if there's a "nothing" then drop all value members after the first "nothing".
val fields = {if (fieldData.map(n=>n.fieldType).contains("nothing")) fieldData.reverse.dropWhile(valueMember =>  valueMember.fieldType != "nothing").reverse; else fieldData}
var l0: Label = null

if (fieldData.length > 0) {
  mv.visitVarInsn(ALOAD, 0);
  mv.visitVarInsn(ALOAD, 1);
  l0 = new Label();

  if (fieldData.map(n => n.fieldType).forall(t => List("nothing").contains(t))) {//if all the valueMembers are "nothing"
    mv.visitJumpInsn(IF_ACMPNE, l0);
    mv.visitInsn(ICONST_1);
    val nothingLabel = new Label();
    mv.visitJumpInsn(GOTO, nothingLabel);
    mv.visitLabel(l0);
    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
  }
  else {
    mv.visitJumpInsn(IF_ACMPEQ, l0);
  }
}
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

if (fieldData.length > 0) mv.visitVarInsn(ASTORE, 4);

var valueMembersGOTOLabel: Label = null
var penultimateLabel: Label = null
var ultimateLabel: Label = null

fields.foreach(valueMember => {
  valueMember.fieldType match {
    case "boolean"|"byte"|"char"|"short"|"int" => {
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
      mv.visitVarInsn(ALOAD, 4);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
      if (fieldData.indexOf(valueMember) == 0) valueMembersGOTOLabel = new Label();
      mv.visitJumpInsn(IF_ICMPNE, valueMembersGOTOLabel);
    }
    case "double"|"float"|"long" => {
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
      mv.visitVarInsn(ALOAD, 4);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
      if (valueMember.fieldType == "double") mv.visitInsn(DCMPL);
      else if (valueMember.fieldType == "float") mv.visitInsn(FCMPL);
      else if (valueMember.fieldType == "long") mv.visitInsn(LCMP);
      if (fieldData.indexOf(valueMember) == 0) valueMembersGOTOLabel = new Label();
      mv.visitJumpInsn(IFNE, valueMembersGOTOLabel);
    }
    case "string"|"null"|"unit"|"option"|"list"|"stream" => {
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
      if (valueMember.fieldType == null) {
        mv.visitInsn(POP);
        mv.visitInsn(ACONST_NULL);
      }
      mv.visitVarInsn(ALOAD, 4);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeDescriptor);
      if (valueMember.fieldType == null) {
        mv.visitInsn(POP);
        mv.visitInsn(ACONST_NULL);
      }
      mv.visitVarInsn(ASTORE, 5);
      mv.visitInsn(DUP);
      val l4 = new Label();
      mv.visitJumpInsn(IFNONNULL, l4);
      mv.visitInsn(POP);
      mv.visitVarInsn(ALOAD, 5);
      val l5 = new Label();
      mv.visitJumpInsn(IFNULL, l5);
      if (fieldData.indexOf(valueMember) == 0) valueMembersGOTOLabel = new Label();
      mv.visitJumpInsn(GOTO, valueMembersGOTOLabel);
      mv.visitLabel(l4);
      if (valueMember.fieldType == "null") {
        mv.visitFrame(Opcodes.F_FULL, 6, Array[Object] (caseClassName, "java/lang/Object", "java/lang/Object", Opcodes.INTEGER,  caseClassName, Opcodes.NULL), 1, Array[Object] (Opcodes.NULL));
      }
      else {
        mv.visitFrame(Opcodes.F_FULL, 6, Array[Object] (caseClassName, "java/lang/Object", "java/lang/Object", Opcodes.INTEGER, caseClassName, valueMember.typeDescriptor.drop(1).dropRight(1)), 1, Array[Object] (valueMember.typeDescriptor.drop(1).dropRight(1)));
      }
      mv.visitVarInsn(ALOAD, 5);
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
      mv.visitJumpInsn(IFEQ, valueMembersGOTOLabel);
      mv.visitLabel(l5);
      mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }
    case "any"|"anyref"|"object" => {
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()Ljava/lang/Object;");
      mv.visitVarInsn(ALOAD, 4);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()Ljava/lang/Object;");
      mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "equals", "(Ljava/lang/Object;Ljava/lang/Object;)Z");
      if (fieldData.indexOf(valueMember) == 0) valueMembersGOTOLabel = new Label();
      mv.visitJumpInsn(IFEQ, valueMembersGOTOLabel);
      mv.visitVarInsn(ALOAD, 4);
    }  
    case "nothing" => {
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()Lscala/runtime/Nothing$;");
      mv.visitInsn(ATHROW);
    }
    case _ => println("unsupported type")
  }
})
//do the following unless there is was "nothing" type and we broke out with a ATHROW instead
if (!fieldData.map(n => n.fieldType).contains("nothing")) mv.visitVarInsn(ALOAD, 4);
//if all value members are of type "nothing", skip the canEqual portion and go to the final portion
if (!fieldData.map(n => n.fieldType).forall(t => List("nothing").contains(t))) {
  fields.contains("nothing") match {
    case true => { //if there is a "nothing" type, then it will be the last value member, and the canEqual is skipped
      mv.visitLabel(valueMembersGOTOLabel);
      mv.visitFrame(Opcodes.F_APPEND,1, Array[Object] (caseClassName), 0, null);
      mv.visitInsn(ICONST_0);
      mv.visitJumpInsn(IFEQ, l3);
      mv.visitLabel(l0);
      mv.visitFrame(Opcodes.F_CHOP,3, null, 0, null);
    }
    case false => {
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, "canEqual", "(Ljava/lang/Object;)Z");
      mv.visitJumpInsn(IFEQ, valueMembersGOTOLabel);
      mv.visitInsn(ICONST_1);
      penultimateLabel = new Label();
      mv.visitJumpInsn(GOTO, penultimateLabel);
      mv.visitLabel(valueMembersGOTOLabel);
  //if all value members are from this list, then:
      if (fieldData.map(n => n.fieldType).forall(t => List("any", "anyref", "boolean", "byte", "char", "int", "double", "float", "long", "short", "object").contains(t))) { 
        mv.visitFrame(Opcodes.F_APPEND,1, Array[Object] (caseClassName), 0, null);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(penultimateLabel);
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] (Opcodes.INTEGER));
        mv.visitJumpInsn(IFEQ, l3);
        mv.visitLabel(l0);
        mv.visitFrame(Opcodes.F_CHOP,3, null, 0, null);
      }
      if (List("string", "unit", "list", "option", "null").contains(fieldData.head.fieldType)){
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(penultimateLabel);
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] (Opcodes.INTEGER));
        mv.visitJumpInsn(IFEQ, l3);
        mv.visitLabel(l0);
        mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, "java/lang/Object"), 0, Array[Object] ());
      }
      else if (List("any", "anyref", "boolean", "byte", "char", "int", "double", "float", "long", "short", "object").contains(fieldData.head)){
        mv.visitFrame(Opcodes.F_CHOP,1, null, 0, null);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(penultimateLabel);
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] (Opcodes.INTEGER));
        mv.visitJumpInsn(IFEQ, l3);
        mv.visitLabel(l0);
        mv.visitFrame(Opcodes.F_CHOP,3, null, 0, null);
      }
    }
  }
  mv.visitInsn(ICONST_1);
  ultimateLabel = new Label();
  mv.visitJumpInsn(GOTO, ultimateLabel);
  mv.visitLabel(l3);
  mv.visitFrame(Opcodes.F_APPEND,2, Array[Object] ("java/lang/Object", Opcodes.INTEGER), 0, null);
}
else {
  mv.visitLabel(l3);
  mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
}
mv.visitInsn(ICONST_0);
mv.visitLabel(ultimateLabel);
mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, "java/lang/Object"), 1, Array[Object] (Opcodes.INTEGER));
mv.visitInsn(IRETURN);
mv.visitMaxs(2, 6);
mv.visitEnd();


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

//-----------------Begin Module Section-------------------

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
mv_MODULE.visitLdcInsn(classData.className);
//mv_MODULE.visitLdcInsn(className);
mv_MODULE.visitInsn(ARETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();

mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "apply", "(" + fieldData.map(fd => fd.typeDescriptor).mkString + ")L" + caseClassName + ";", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitTypeInsn(NEW, caseClassName);
mv_MODULE.visitInsn(DUP);

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


mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
mv_MODULE.visitCode();
mv_MODULE.visitVarInsn(ALOAD, 0);
mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/runtime/AbstractFunction" + fieldData.length, "<init>", "()V");
mv_MODULE.visitVarInsn(ALOAD, 0);
mv_MODULE.visitFieldInsn(PUTSTATIC, caseClassName + "$", "MODULE$", "L" + caseClassName + "$;");
mv_MODULE.visitInsn(RETURN);
mv_MODULE.visitMaxs(1, 1);
mv_MODULE.visitEnd();

cw_MODULE.visitEnd();

List(cw.toByteArray(), cw_MODULE.toByteArray());

}
}
