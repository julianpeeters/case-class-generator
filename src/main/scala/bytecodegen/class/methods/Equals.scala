package models
import avocet._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

case class Equals(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    
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


  }
}
