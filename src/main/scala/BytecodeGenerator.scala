package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._

object BytecodeGenerator {

  def dump(classData: ClassData): List[Array[Byte]] = {

    val name = classData.className

    val namespace: Option[String] = {
      if (classData.classNamespace.isDefined) Some(classData.classNamespace.get.replaceAllLiterally(".", "/"))
      else None
    }

    val caseClassName = {
      if (namespace.isDefined) namespace.get + "/" + name
      else name
    }

    val typedFields: List[TypedFields] = classData.classFields.map(field => FieldMatcher.enrichFieldData(namespace, field))

    val potentialNamespace = {
      if (classData.classNamespace.isDefined) classData.classNamespace.get
      else "<empty>"
    }

    val mySig = new ScalaSig(List("case class"), List(potentialNamespace, classData.className), typedFields.map(f => (f.fieldName, f.fieldType)))

    //generate a pair of class and module class
    List(new MyRecordDump().dump(mySig, caseClassName, typedFields),
      new MyRecord$Dump().dump(caseClassName, typedFields)) // $Dump is ASM's convention for naming the module class

  }

}

