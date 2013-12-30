package caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


//HashCode has two methods: the main "dump" method, and the helper "matchFields" That it calls
case class HashCode(cw: ClassWriter, var mv: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {

  val userDefinedTypes = CaseClassGenerator.generatedClasses.keys.toList

  def dump = {
    mv = cw.visitMethod(ACC_PUBLIC, "hashCode", "()I", null, null);
    mv.visitCode();
    fieldData.length match {
      case 0 => {
        mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_hashCode", "(Lscala/Product;)I");
      }
      case x if x > 0 => { //type erase the generics, then check if all the types are from the folling list
        if (fieldData.map(fd => fd.fieldType.takeWhile(c => c != '[')).forall(t => (List("Nothing", "Null", "Any", "AnyRef", "Object", "String", "List", "Option", "Stream" ):::userDefinedTypes).contains(t))) {//if all the valueMembers are in this list (of "empty" types, look different when paired with "real")   
  
          mv.visitFieldInsn(GETSTATIC, "scala/runtime/ScalaRunTime$", "MODULE$", "Lscala/runtime/ScalaRunTime$;");
          mv.visitVarInsn(ALOAD, 0);
          mv.visitMethodInsn(INVOKEVIRTUAL, "scala/runtime/ScalaRunTime$", "_hashCode", "(Lscala/Product;)I");
        } 

        else { 
          mv.visitLdcInsn(new Integer(-889275714));
          mv.visitVarInsn(ISTORE, 1);
          mv.visitVarInsn(ILOAD, 1);

          val fields = if (fieldData.map(n=>n.fieldType).contains("Nothing")) fieldData.reverse.dropWhile(valueMember =>   valueMember.fieldType != "Nothing").reverse; else fieldData

    //if there is more than one non-"empty" type(see the list above), drop all types after the first "Nothing".
          fields.foreach( valueMember =>  matchFields(valueMember) )    

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

        if (!fieldData.map(n => n.fieldType).contains("Nothing")) mv.visitInsn(IRETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
      }
    }
  }


  def matchFields(valueMember: FieldData) = {
    valueMember.fieldType match { 
              case "Byte"|"Char"|"Short"|"Int"|"Long"|"Float"|"Double"|"Unit"|"Null" => {
                valueMember.fieldType match {
                  case "Byte"|"Short"|"Int"|"Char" => { 
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
                  }
                  case "Long"|"Float"|"Double" => {
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
                    mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", valueMember.fieldType + "Hash", "(" + valueMember.typeData.typeDescriptor + ")I");
                  }
                  case "Unit"|"Null" => {
                    mv.visitInsn(ICONST_0);
                    mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
                    mv.visitVarInsn(ISTORE, 1);
                    mv.visitVarInsn(ILOAD, 1);
                  }
                  case _ => error("could not generate hashcode method: unsupported type")
                }
              }
              case "Boolean" => {
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
                mv.visitFrame(Opcodes.F_FULL, 2, Array[Object] (caseClassName, Opcodes.INTEGER), 2, Array[Object] (Opcodes.INTEGER,  Opcodes.INTEGER));
                mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
                mv.visitVarInsn(ISTORE, 1);
                mv.visitVarInsn(ILOAD, 1);
              }
        //if there were only one valueMember, the "if" statement would have taken care of things
        //so this has to have come after

              case "Any"|"AnyRef"|"Object"|"String"|"List"|"Stream"|"Option" => {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
                mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "anyHash", "(Ljava/lang/Object;)I");
              }
              case "Nothing" => { //if "Nothing" is a value member's type, it will be the last one in the list of value members
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()Lscala/runtime/Nothing$;");
                mv.visitInsn(ATHROW);
              }
              case name: String if userDefinedTypes.contains(name) => {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKEVIRTUAL, caseClassName, valueMember.fieldName, "()" + valueMember.typeData.typeDescriptor);
                mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "anyHash", "(Ljava/lang/Object;)I");
              }        
              case _ => error("cannot generate HashCode method: unsupported type")
            } 
      //Booleans and Units get special treatment because their ASM lines have a "mix" already
            if (valueMember.fieldType != "Boolean" && valueMember.fieldType != "Unit" && valueMember.fieldType != "Null") {  
              mv.visitMethodInsn(INVOKESTATIC, "scala/runtime/Statics", "mix", "(II)I");
              mv.visitVarInsn(ISTORE, 1);
              mv.visitVarInsn(ILOAD, 1);
            }

  }


}
