package models
import avocet._
import org.objectweb.asm._
import Opcodes._



case class MyRecordDump {

  def dump(mySig: ScalaSig, caseClassName: String, fieldData: List[FieldData]): Array[Byte] = {

    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);//|ClassWriter.COMPUTE_FRAMES); //now visit max's args don't matter
    var fv: FieldVisitor = null
    var mv: MethodVisitor = null
    var av0: AnnotationVisitor = null
    val ctorReturnType = "(" + fieldData.map(n => n.typeDescriptor ).mkString + ")V"

  ClassHeader(cw, caseClassName).dump
  ScalaSigAnnotation(cw, av0, mySig).dump
  Fields(cw, fieldData).dump

  if (fieldData.length > 1) Tupled(cw, mv, caseClassName, fieldData).dump; Curried(cw, mv, caseClassName).dump

  FieldMethods(cw, mv, caseClassName, fieldData).dump//"FieldMethods"for lack of a better name
  Copy(cw, mv, caseClassName, fieldData).dump
  CopyDefault(cw, mv, caseClassName, fieldData).dump
  ProductPrefix(cw, mv, caseClassName.dropWhile(c => c != '/').drop(1), fieldData).dump//3rd arg is the label of the class
  ProductArity(cw, mv, fieldData).dump
  ProductElement(cw, mv, caseClassName, fieldData).dump
  ProductIterator(cw, mv).dump
  CanEqual(cw, mv, caseClassName).dump
  HashCode(cw, mv, caseClassName, fieldData).dump
  ToString(cw, mv).dump
  Equals(cw, mv, caseClassName, fieldData).dump
  Init(cw, mv, caseClassName, fieldData, ctorReturnType).dump
  cw.toByteArray()
}
}
