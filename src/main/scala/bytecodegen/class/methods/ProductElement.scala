package models
import avocet._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

case class ProductElement(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
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
  }
}
