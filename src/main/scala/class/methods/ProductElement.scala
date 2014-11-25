package com.julianpeeters.caseclass.generator

import scala.reflect.runtime.universe._
import org.objectweb.asm._
import Opcodes._

case class ProductElement(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[EnrichedField]) {
  def dump = {
    mv = cw.visitMethod(ACC_PUBLIC, "productElement", "(I)Ljava/lang/Object;", null, null);
    mv.visitCode();
    mv.visitVarInsn(ILOAD, 1);
    mv.visitVarInsn(ISTORE, 2);
    mv.visitVarInsn(ILOAD, 2);
    val labels = (0 to fieldData.length).map(l => new Label())
    val params = labels.take(fieldData.length)
    mv.visitTableSwitchInsn(0, labels.length - 2, labels(fieldData.length), params: _*);
    mv.visitLabel(labels(fieldData.length));
    mv.visitFrame(Opcodes.F_APPEND, 1, Array[Object] (Opcodes.INTEGER), 0, null);
    mv.visitTypeInsn(NEW, "java/lang/IndexOutOfBoundsException");
    mv.visitInsn(DUP);
    mv.visitVarInsn(ILOAD, 1);
    mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;");
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IndexOutOfBoundsException", "<init>", "(Ljava/lang/String;)V");
    mv.visitInsn(ATHROW);
    mv.visitLabel(labels(fieldData.length - 1));
    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

    val reversed = fieldData.reverse
    var terminalLabel: Label = null
    reversed.take(fieldData.length).foreach(valueMember => {
      mv.visitVarInsn(ALOAD, 0);
      val tpe = {
        if (valueMember.typeData.typeDescriptor == "Lscala/runtime/BoxedUnit;") "V"
        else valueMember.typeData.typeDescriptor
      }

      mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + tpe);

      valueMember.fieldType match {

        case l if l =:= typeOf[Byte]       => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToByte", "(B)Ljava/lang/Byte;");
        case l if l =:= typeOf[Short]      => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToShort", "(S)Ljava/lang/Short;");
        case l if l =:= typeOf[Int]        => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");

        case l if l =:= typeOf[Long]       => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToLong", "(J)Ljava/lang/Long;");

        case l if l =:= typeOf[Float]      => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToFloat", "(F)Ljava/lang/Float;");
        case l if l =:= typeOf[Double]     => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToDouble", "(D)Ljava/lang/Double;");
        case l if l =:= typeOf[Char]       => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToCharacter", "(C)Ljava/lang/Character;");

        case x if x =:= typeOf[String]     =>

        case l if l =:= typeOf[Boolean]    => mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
        case l if l =:= typeOf[Unit]       => mv.visitFieldInsn(GETSTATIC, "scala/runtime/BoxedUnit", "UNIT", "Lscala/runtime/BoxedUnit;");
        case l if l =:= typeOf[Null]       => mv.visitInsn(POP); mv.visitInsn(ACONST_NULL);
        case l if l =:= typeOf[Nothing]    => mv.visitInsn(ATHROW);
        case l if l =:= typeOf[Any]        => 
        case l if l =:= typeOf[AnyRef]     => 
        case l if l <:< typeOf[Option[Any]]     => 
        case l if l <:< typeOf[Object]     => 


        //nothing needed for generics such as list, option, nor for user-defined types
        case TypeRef(pre, symbol, args) =>
        case _            => error("cannot generate productElement method: unsupported type")

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
