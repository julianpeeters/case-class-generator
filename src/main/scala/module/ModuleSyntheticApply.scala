package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import org.objectweb.asm._
import Opcodes._

case class ModuleSyntheticApply(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String, fieldData: List[TypedFields]) {
  def dump = {

    //  val userDefinedTypes = CaseClassGenerator.generatedClasses.keys.map(k => k.dropWhile(c => (c != '.')).tail).toList
    //      val userDefinedTypes = CaseClassGenerator.generatedClasses.keys.toList
    val userDefinedTypes = ClassStore.generatedClasses.keys.toList

    mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "apply", "(" + Stream.continually("Ljava/lang/Object;").take(fieldData.length).mkString + ")Ljava/lang/Object;", null, null);
    mv_MODULE.visitCode();
    mv_MODULE.visitVarInsn(ALOAD, 0);

    fieldData.zipWithIndex.foreach(n => {
      //erase types and then match types
      n._1.fieldType.takeWhile(c => c != '[') match {
        case "String" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "java/lang/String");
        }
        case "Int" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToInt", "(Ljava/lang/Object;)I");
        }
        case "Boolean" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToBoolean", "(Ljava/lang/Object;)Z");
        }
        case "Short" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToShort", "(Ljava/lang/Object;)S");
        }
        case "Long" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToLong", "(Ljava/lang/Object;)J");
        }
        case "Float" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToFloat", "(Ljava/lang/Object;)F");
        }
        case "Double" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToDouble", "(Ljava/lang/Object;)D");
        }
        case "Byte" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToByte", "(Ljava/lang/Object;)B");
        }
        case "Char" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToChar", "(Ljava/lang/Object;)C");
        }
        case "Unit" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/BoxedUnit");
        }
        case "Null" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/Null$");
        }
        case "Nothing" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/Nothing$");
        }
        case "Any"    => mv_MODULE.visitVarInsn(ALOAD, n._2);
        case "AnyRef" => mv_MODULE.visitVarInsn(ALOAD, n._2);
        case "Object" => mv_MODULE.visitVarInsn(ALOAD, n._2);
        case "List" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/collection/immutable/List")
        }
        case "Option" => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/Option")
        }
        case "bytes" => //TODO move this into avro datafile parser

        case name: String if userDefinedTypes.contains(name) => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          //add namespace to the type name
          println("caseClassName")
          val fullName = caseClassName.takeWhile(c => (c != '/')) + "/" + name
          println("fullName " + fullName)
          mv_MODULE.visitTypeInsn(CHECKCAST, fullName);
        }

        case _ => error("cannot generate synthetic apply method: unsupported type")
      }
    })
    mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "apply", "(" + fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ")L" + caseClassName + ";");
    mv_MODULE.visitInsn(ARETURN);
    mv_MODULE.visitMaxs(4, 4);
    mv_MODULE.visitEnd();
  }
}
