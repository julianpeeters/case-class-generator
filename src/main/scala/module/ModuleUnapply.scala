package com.julianpeeters.caseclass.generator

import scala.reflect.runtime.universe._
import org.objectweb.asm._
import Opcodes._

case class ModuleUnapply(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String, fieldData: List[EnrichedField]) {

  def dump = {

    val userDefinedTypes = ClassStore.generatedClasses.keys.toList

    fieldData.length match {
      case 1 => {
        mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(L" + caseClassName + ";)Lscala/Option;", "(" + caseClassName + ";)Lscala/Option<" + fieldData.map(fd => fd.typeData.unapplyType).mkString + ">;", null);

      }
      case x: Int if x > 1 => {
        mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(L" + caseClassName + ";)Lscala/Option;", "(" + caseClassName + ";)Lscala/Option<Lscala/Tuple" + fieldData.length + "<" + fieldData.map(fd => fd.typeData.unapplyType).mkString + ">;>;", null);
      }
    }

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

    if (fieldData.length > 1) {
      mv_MODULE.visitTypeInsn(NEW, "scala/Tuple" + fieldData.length);
      mv_MODULE.visitInsn(DUP);
    }

    fieldData.foreach(fd => {
      fd.fieldType match {

        case l if l =:= typeOf[Int] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
        }
        case l if l =:= typeOf[Boolean] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
        }
        case l if l =:= typeOf[Long] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToLong", "(J)Ljava/lang/Long;");
        }
        case l if l =:= typeOf[Double] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToDouble", "(D)Ljava/lang/Double;");
        }
        case l if l =:= typeOf[Float] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToFloat", "(F)Ljava/lang/Float;");
        }
        case l if l =:= typeOf[Byte] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToByte", "(B)Ljava/lang/Byte;");
        }
        case l if l =:= typeOf[Short] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToShort", "(S)Ljava/lang/Short;");
        }
        case l if l =:= typeOf[Char] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToCharacter", "(C)Ljava/lang/Character;");
        }

        case s if s =:= typeOf[String] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
        }

        case s if s =:= typeOf[Unit] => {
          mv_MODULE.visitFieldInsn(GETSTATIC, "scala/runtime/BoxedUnit", "UNIT", "Lscala/runtime/BoxedUnit;");
        }
        case s if s =:= typeOf[Null] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitInsn(POP);
          mv_MODULE.visitInsn(ACONST_NULL);
        }
        case s if s =:= typeOf[Nothing] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
          mv_MODULE.visitInsn(ATHROW);
        }
        case s if s =:= typeOf[Any] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
        }
        case s if s =:= typeOf[AnyRef] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
        }
        case s if s =:= typeOf[Object] => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
        }

        //generics
        case la @ TypeRef(pre, symbol, args) if ( (la <:< typeOf[List[Any]] | la <:< typeOf[Option[Any]]) && args.length == 1 ) => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
        }

        // user defined or other case classes
        case name @ TypeRef(pre, symbol, args) if userDefinedTypes.contains(name) => {
          mv_MODULE.visitVarInsn(ALOAD, 1);
          mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
        }

        case _ => error("cannot generate module unapply method, unsupported type")
      }
    })

    if (fieldData.length > 1) {
      mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/Tuple" + fieldData.length, "<init>", "(" + Stream.continually("Ljava/lang/Object;").take(fieldData.length).mkString + ")V");
    }

    mv_MODULE.visitMethodInsn(INVOKESPECIAL, "scala/Some", "<init>", "(Ljava/lang/Object;)V");
    mv_MODULE.visitLabel(l1_MODULE);
    mv_MODULE.visitFrame(Opcodes.F_SAME1, 0, null, 1, Array[Object] ("scala/Option"));
    mv_MODULE.visitInsn(ARETURN);
    mv_MODULE.visitMaxs(7, 2);
    mv_MODULE.visitEnd();
  }
}
