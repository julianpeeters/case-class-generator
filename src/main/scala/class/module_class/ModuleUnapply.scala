package caseclass.generator
import artisinal.pickle.maker._
import org.objectweb.asm._
import Opcodes._


case class ModuleUnapply(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    
//  val userDefinedTypes = CaseClassGenerator.generatedClasses.keys.map(k => k.dropWhile(c => (c != '.')).tail).toList
  val userDefinedTypes = CaseClassGenerator.generatedClasses.keys.toList

    fieldData.length match {
      case 1            => {
        //if it's a user-defined type 
        if (fieldData.head.fieldType == "rec") { println("MU 1 rec, classname if: " +  caseClassName)
          mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(L" + caseClassName + ";)Lscala/Option;", "(" + caseClassName + ";)Lscala/Option<" + fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ">;", null);
        }
        else {
          mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(L" + caseClassName + ";)Lscala/Option;", "(" + caseClassName + ";)Lscala/Option<" + fieldData.map(fd => fd.typeData.unapplyType).mkString + ">;", null);
        }
      }
      case x: Int if x > 1 => {
        mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(L" + caseClassName + ";)Lscala/Option;", "(" + caseClassName + ";)Lscala/Option<Lscala/Tuple" + fieldData.length + "<" + fieldData.map(fd => fd.typeData.unapplyType).mkString + ">;>;", null);

      }
    }

//mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC, "unapply", "(L" + caseClassName + ";)Lscala/Option;", "(" + caseClassName + ";)Lscala/Option<Lscala/Tuple" + fieldData.length + "<" + fieldData.map(fd => fd.typeData.unapplyType).mkString + ">;>;", null);
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
    case "Int"     => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToInteger", "(I)Ljava/lang/Integer;");
    }
    case "Boolean" => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToBoolean", "(Z)Ljava/lang/Boolean;");
    }
    case "Long"    => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToLong", "(J)Ljava/lang/Long;");
    }

    case "Double"  => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToDouble", "(D)Ljava/lang/Double;");
    }
    case "Float"   => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToFloat", "(F)Ljava/lang/Float;");
    }
    case "Byte"    => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToByte", "(B)Ljava/lang/Byte;");
    }
    case "Short"   => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToShort", "(S)Ljava/lang/Short;");
    }
    case "Char"    => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "boxToCharacter", "(C)Ljava/lang/Character;");
    }
    case "String"  => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);    
    }
    case "Unit"    => {
      mv_MODULE.visitFieldInsn(GETSTATIC, "scala/runtime/BoxedUnit", "UNIT", "Lscala/runtime/BoxedUnit;");
    }
    case "Null"    => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitInsn(POP);
      mv_MODULE.visitInsn(ACONST_NULL);
    }
    case "Nothing" => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
      mv_MODULE.visitInsn(ATHROW);
    }
    case "Any"     => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);    
    }
    case "AnyRef"  => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
    } 
    case "Object"  => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
    }
    case name: String if userDefinedTypes.contains(name)  => {
      mv_MODULE.visitVarInsn(ALOAD, 1);
      mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName, fd.fieldName, "()" + fd.typeData.typeDescriptor);
    }
    case _         => println("cannot generate unapply unsupported type")
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
