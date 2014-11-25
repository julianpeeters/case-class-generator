package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import org.objectweb.asm._
import Opcodes._

object MyRecord$Dumper {

  def dump(caseClassName: String, fieldData: List[EnrichedField]): Array[Byte] = {
    val cw_MODULE: ClassWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    var fv_MODULE: FieldVisitor = null;
    var mv_MODULE: MethodVisitor = null;
    var av0_MODULE: AnnotationVisitor = null;

    ModuleHeader(cw_MODULE, caseClassName, fieldData).dump
    ModuleFields(cw_MODULE, fv_MODULE, caseClassName).dump

    ModuleCinit(cw_MODULE, mv_MODULE, caseClassName).dump
    ModuleToString(cw_MODULE, mv_MODULE, caseClassName).dump
    ModuleApply(cw_MODULE, mv_MODULE, caseClassName, fieldData).dump
    ModuleUnapply(cw_MODULE, mv_MODULE, caseClassName, fieldData).dump
    ModuleReadResolve(cw_MODULE, mv_MODULE, caseClassName).dump
    ModuleSyntheticApply(cw_MODULE, mv_MODULE, caseClassName, fieldData).dump
    ModuleInit(cw_MODULE, mv_MODULE, caseClassName, fieldData).dump

    cw_MODULE.toByteArray()
  }
}
