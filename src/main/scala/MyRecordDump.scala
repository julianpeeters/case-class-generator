package models
import avocet._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

import scala.pickling._
import binary._


import java.util.Arrays
import scala.io.Codec._

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

  def dump: List[Array[Byte]] = {

    val caseClassName = "models/MyRecord"
    val className = "MyRecord"
    val classNamespace = "models"
    val name = className
   // val fieldData: List[FieldData] = classData.classFields
  //val fieldData: List[FieldData] = List(FieldData("x","string","Ljava/lang/String;", "Ljava/lang/String;",25,176), FieldData("y","int","I", "Ljava/lang/Object;",21,172), FieldData("z","boolean","Z", "Ljava/lang/Object;",21,172))//classData.classFields
    val fieldData: List[FieldData] = List(FieldData("x","string","Ljava/lang/String;", "Ljava/lang/String;",25,176))//classData.classFields
    val ctorReturnType = "(" + fieldData.map(n => n.typeDescriptor ).mkString + ")V"

 
    val mySigRecord = new ScalaSig(List("case class"), List(classNamespace, className), List(("x", "String"), ("y", "Int"), ("z", "Boolean")))


val enc = ByteCodecs.encode(mySigRecord.bytes)
enc.foreach(println)
println(enc.length)

val mySig = new ScalaSig(List("case class"), List(classNamespace, className), List(("x", "String")))
    

    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);//|ClassWriter.COMPUTE_FRAMES); //now visit max's args don't matter
    var fv: FieldVisitor = null
    var mv: MethodVisitor = null
    var av0: AnnotationVisitor = null
//the sig for string, int, boolean



cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, caseClassName, null, "java/lang/Object", Array[String] ( "scala/Product", "scala/Serializable" ));

{
av0 = cw.visitAnnotation("Lscala/reflect/ScalaSignature;", true);
av0.visit("bytes", new String(ByteCodecs.encode(mySig.bytes)));
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
mv.visitLdcInsn(className);
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
mv_MODULE.visitLdcInsn(className);
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
