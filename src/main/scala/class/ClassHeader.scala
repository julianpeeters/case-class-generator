package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

import java.util.Arrays
import scala.io.Codec._
case class ClassHeader(cw: ClassWriter, caseClassName: String) {
  def dump = {
    cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, caseClassName, null, "java/lang/Object", Array[String] ("scala/Product", "scala/Serializable"));
  }
}
