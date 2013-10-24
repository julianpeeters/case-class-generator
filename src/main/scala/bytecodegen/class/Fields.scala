package caseclass.generator
import artisinal.pickle.maker._
import scala.reflect.internal.pickling._
import org.objectweb.asm._
import Opcodes._


import java.util.Arrays
import scala.io.Codec._
case class Fields(cw: ClassWriter, fieldData: List[FieldData]) {
  def dump = {
    fieldData.foreach(n=>(cw.visitField(ACC_PRIVATE + ACC_FINAL, n.fieldName, n.typeDescriptor.toString, null, null).visitEnd())) 

  }
}
