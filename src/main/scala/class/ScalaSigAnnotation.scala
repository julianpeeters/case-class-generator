package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._

import java.util.Arrays
import scala.io.Codec._
case class ScalaSigAnnotation(cw: ClassWriter, var av0: AnnotationVisitor, mySig: ScalaSig) {

  def dump = {
    av0 = cw.visitAnnotation("Lscala/reflect/ScalaSignature;", true);
    av0.visit("bytes", mySig.bytes);
    av0.visitEnd();
  }

}
