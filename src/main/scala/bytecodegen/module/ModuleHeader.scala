package caseclass.generator
import artisinal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class ModuleHeader(cw_MODULE: ClassWriter, caseClassName: String, fieldData: List[FieldData]) {
  def dump = {
    cw_MODULE.visit(V1_6, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, caseClassName + "$", "Lscala/runtime/AbstractFunction" + fieldData.length + "<" + fieldData.map(fd => fd.unapplyType).mkString + "L" + caseClassName + ";>;Lscala/Serializable;", "scala/runtime/AbstractFunction" + fieldData.length, Array[String] ("scala/Serializable" ));

  }
}
