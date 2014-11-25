package com.julianpeeters.caseclass.generator

import scala.reflect.runtime.universe._
import org.objectweb.asm._
import Opcodes._

case class ModuleSyntheticApply(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String, fieldData: List[EnrichedField]) {
  def dump = {

    val userDefinedTypes = ClassStore.generatedClasses.keys.toList

    mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "apply", "(" + Stream.continually("Ljava/lang/Object;").take(fieldData.length).mkString + ")Ljava/lang/Object;", null, null);
    mv_MODULE.visitCode();
    mv_MODULE.visitVarInsn(ALOAD, 0);

    fieldData.zipWithIndex.foreach(n => {
      n._1.fieldType match {
        case s if s =:= typeOf[String] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "java/lang/String");
        }
        case i if i =:= typeOf[Int] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToInt", "(Ljava/lang/Object;)I");
        }
        case z if z =:= typeOf[Boolean] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToBoolean", "(Ljava/lang/Object;)Z");
        }
        case s if s =:= typeOf[Short] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToShort", "(Ljava/lang/Object;)S");
        }
        case l if l =:= typeOf[Long] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToLong", "(Ljava/lang/Object;)J");
        }
        case s if s =:= typeOf[Float] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToFloat", "(Ljava/lang/Object;)F");
        }
        case s if s =:= typeOf[Double] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToDouble", "(Ljava/lang/Object;)D");
        }
        case s if s =:= typeOf[Byte] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToByte", "(Ljava/lang/Object;)B");
        }
        case s if s =:= typeOf[Char] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitMethodInsn(INVOKESTATIC, "scala/runtime/BoxesRunTime", "unboxToChar", "(Ljava/lang/Object;)C");
        }
        case s if s =:= typeOf[Unit] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/BoxedUnit");
        }
        case s if s =:= typeOf[Null] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/Null$");
        }
        case s if s =:= typeOf[Nothing] => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/runtime/Nothing$");
        }
        case s if s =:= typeOf[Any]    => mv_MODULE.visitVarInsn(ALOAD, n._2);
        case s if s =:= typeOf[AnyRef] => mv_MODULE.visitVarInsn(ALOAD, n._2);
        case s if s =:= typeOf[Object] => mv_MODULE.visitVarInsn(ALOAD, n._2);

        case la @ TypeRef(pre, symbol, args) if (la <:< typeOf[List[Any]] && args.length == 1) => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/collection/immutable/List")
        }

        case la @ TypeRef(pre, symbol, args) if (la <:< typeOf[Option[Any]] && args.length == 1) => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          mv_MODULE.visitTypeInsn(CHECKCAST, "scala/Option")
        }

        case name @ TypeRef(pre, symbol, args) if userDefinedTypes.contains(name) => {
          mv_MODULE.visitVarInsn(ALOAD, n._2);
          //add namespace to the type name
          val fullName = name.toString.replaceAllLiterally(".", "/")//caseClassName.takeWhile(c => (c != '/')) + "/" + name
          mv_MODULE.visitTypeInsn(CHECKCAST, fullName);
        }

        case x => error("cannot generate synthetic apply method of the module class: unsupported type: " + x)
      }
    })
    mv_MODULE.visitMethodInsn(INVOKEVIRTUAL, caseClassName + "$", "apply", "(" + fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ")L" + caseClassName + ";");
    mv_MODULE.visitInsn(ARETURN);
    mv_MODULE.visitMaxs(4, 4);
    mv_MODULE.visitEnd();
  }
}
