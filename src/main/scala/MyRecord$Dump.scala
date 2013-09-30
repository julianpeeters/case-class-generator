package models
import avocet._
import org.objectweb.asm._
import Opcodes._



object MyRecord$Dump {

  def dump(name: String, caseClassName: String, fieldData: List[FieldData]): Array[Byte] = {
    val cw_MODULE: ClassWriter = new ClassWriter(0);
    var fv_MODULE: FieldVisitor = null;
    var mv_MODULE: MethodVisitor = null;
    var av0_MODULE: AnnotationVisitor = null;

    ModuleHeader(cw_MODULE, caseClassName, fieldData).dump
    ModuleFields(cw_MODULE, fv_MODULE, caseClassName).dump

    ModuleCinit(cw_MODULE, mv_MODULE, caseClassName).dump
    ModuleToString(cw_MODULE, mv_MODULE, name).dump
    ModuleApply(cw_MODULE, mv_MODULE, caseClassName, fieldData).dump
    ModuleUnapply(cw_MODULE, mv_MODULE, caseClassName, fieldData).dump
    ModuleReadResolve(cw_MODULE, mv_MODULE, caseClassName).dump
    ModuleSyntheticApply(cw_MODULE, mv_MODULE, caseClassName, fieldData).dump
    ModuleInit(cw_MODULE, mv_MODULE, caseClassName, fieldData).dump

    cw_MODULE.toByteArray()
  }
}
