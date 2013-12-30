package caseclass.generator
import artisanal.pickle.maker._
import org.objectweb.asm._
import Opcodes._


case class ModuleApply(cw_MODULE: ClassWriter, var mv_MODULE: MethodVisitor, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {

    if ( fieldData.map(fd => fd.fieldType).exists(ft => ft.endsWith("]"))) {
      mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC,
        "apply", 
        "(" + fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ")L" + caseClassName + ";",
        "(" + fieldData.map(fd => fd.typeData.unerasedTypeDescriptor).mkString + ")L" + caseClassName + ";",
        null);
    }
    else mv_MODULE = cw_MODULE.visitMethod(ACC_PUBLIC,
        "apply", 
        "(" + fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ")L" + caseClassName + ";", 
        null,
        null);

    mv_MODULE.visitCode();
    mv_MODULE.visitTypeInsn(NEW, caseClassName);
    mv_MODULE.visitInsn(DUP);

    var argIndex = 1

    fieldData.map(fd => fd.typeData.loadInstr).foreach(lI => {
      if (lI == DLOAD | lI == LLOAD ) {
        mv_MODULE.visitVarInsn(lI, argIndex); 
        argIndex += 2
      }
      else { 
        mv_MODULE.visitVarInsn(lI, argIndex); 
        argIndex += 1;
      }
    })


    mv_MODULE.visitMethodInsn(INVOKESPECIAL, caseClassName, "<init>", "(" + fieldData.map(fd => fd.typeData.typeDescriptor).mkString + ")V");
    mv_MODULE.visitInsn(ARETURN);
    mv_MODULE.visitMaxs(5, 4);
    mv_MODULE.visitEnd();

  }
}
