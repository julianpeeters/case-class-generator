package caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class ModuleFields(cw_MODULE: ClassWriter, var fv_MODULE: FieldVisitor, caseClassName: String) {
  def dump = {
    fv_MODULE = cw_MODULE.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "MODULE$", "L" + caseClassName + "$;", null, null);
fv_MODULE.visitEnd();

  }
}
