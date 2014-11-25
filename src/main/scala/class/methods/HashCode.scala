package com.julianpeeters.caseclass.generator

import org.objectweb.asm._
import Opcodes._
import scala.reflect.runtime.universe._

//HashCode has two methods: the main "dump" method, and the helper "matchFields" that it calls
case class HashCode(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[EnrichedField]) {

  val userDefinedTypes = ClassStore.generatedClasses.keys.toList

  def dump = {
    mv = cw.visitMethod(ACC_PUBLIC, "hashCode", "()I", null, null);
    mv.visitCode();
    fieldData.length match {
      case 0 => {
        mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_hashCode", "(Lscala/Product;)I");
      }
      case x if x > 0 => { //type erase the generics, then check if all the types are from the following list
        if (fieldData.map(fd => fd.fieldType.erasure).forall(t => (List(typeOf[Nothing].erasure, typeOf[Null].erasure, typeOf[Any].erasure, typeOf[AnyRef].erasure, typeOf[Object].erasure, typeOf[String].erasure, typeOf[Option[Any]].erasure, typeOf[List[Any]].erasure) ::: userDefinedTypes).contains(t))) { //if all the valueMembers are in this list (of "empty" types, look different when paired with "real")   
          mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
          mv.visitVarInsn(ALOAD, 0);
          mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_hashCode", "(Lscala/Product;)I");
        }

        else {
          mv.visitLdcInsn(new Integer(-889275714));
          mv.visitVarInsn(ISTORE, 1);
          mv.visitVarInsn(ILOAD, 1);

          val fields = if (fieldData.map(n => n.fieldType).contains(typeOf[Nothing])) fieldData.reverse.dropWhile(valueMember => valueMember.fieldType != typeOf[Nothing]).reverse; else fieldData

          // if there is more than one non-"empty" type(see the list above), drop all types after the first Nothing.
          fields.foreach(valueMember => matchFields(valueMember))

          fieldData.length match {
            case 1          => mv.visitInsn(ICONST_1);
            case 2          => mv.visitInsn(ICONST_2);
            case 3          => mv.visitInsn(ICONST_3);
            case 4          => mv.visitInsn(ICONST_4);
            case 5          => mv.visitInsn(ICONST_5);
            case x if x > 5 => mv.visitIntInsn(BIPUSH, x);
          }
          mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "finalizeHash", "(II)I");
        }

        if (!fieldData.map(n => n.fieldType).contains(typeOf[Nothing])) mv.visitInsn(IRETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
      }
    }
  }

  def matchFields(valueMember: EnrichedField) = {  

    valueMember.fieldType match {
      case l if (l =:= typeOf[Byte] | 
                 l =:= typeOf[Char] | 
                 l =:= typeOf[Short] | 
                 l =:= typeOf[Int] | 
                 l =:= typeOf[Long] | 
                 l =:= typeOf[Float] |
                 l =:= typeOf[Double] | 
                 l =:=typeOf[Unit] | 
                 l =:=typeOf[Null] ) => {
        valueMember.fieldType match {
          case  l if (l =:= typeOf[Byte] | l =:= typeOf[Short] | l =:= typeOf[Int] | l =:= typeOf[Char]) => {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
          }
          case l if (l =:= typeOf[Long] | l =:= typeOf[Float] | l =:= typeOf[Double]) => {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
            mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", valueMember.fieldType + "Hash", "(" + valueMember.typeData.typeDescriptor + ")I");
          }
          case l if (l =:= typeOf[Unit] | l =:=  typeOf[Null]) => {
            mv.visitInsn(ICONST_0);
            mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
            mv.visitVarInsn(ISTORE, 1);
            mv.visitVarInsn(ILOAD, 1);
          }
          case _ => error("could not generate hashcode method: unsupported type")
        }
      }

      case b if b =:= typeOf[Boolean] => {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
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
      }
      //if there were only one valueMember, the "if" statement would have taken care of things
      //so this has to have come after
      case la @ TypeRef(pre, symbol, args) if ((la <:< typeOf[List[Any]] | la <:< typeOf[Option[Any]] | la <:< typeOf[Stream[Any]]) && args.length == 1 ) => { 
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
        mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "anyHash", "(Ljava/lang/Object;)I");
      }
      case s if (s =:= typeOf[String] | s =:= typeOf[Object] | s =:= typeOf[Any] | s =:= typeOf[AnyRef]) => {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
        mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "anyHash", "(Ljava/lang/Object;)I");
      }
      case n if n =:= typeOf[Nothing] => { //if typeOf[Nothing] is a value member's type, it will be the last one in the list of value members
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()Lscala/runtime/Nothing$;");
        mv.visitInsn(ATHROW);
      }
      // user defined or other case classes
      case name @ TypeRef(pre, symbol, args) if userDefinedTypes.contains(name) => {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
        mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "anyHash", "(Ljava/lang/Object;)I");
      }

      case _ => error("cannot generate HashCode method: unsupported type")
    }
    //Booleans and Units get special treatment because their ASM lines have a "mix" already
    if (valueMember.fieldType != typeOf[Boolean] && valueMember.fieldType != typeOf[Unit] && valueMember.fieldType != typeOf[Null]) { 

      mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
      mv.visitVarInsn(ISTORE, 1);
      mv.visitVarInsn(ILOAD, 1);
    }

  }

}
