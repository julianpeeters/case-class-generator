package com.julianpeeters.caseclass.generator
import artisanal.pickle.maker._
import scala.reflect.runtime.universe._

case class ClassBytes(bytes: Array[Byte])
case class ModuleBytes(bytes: Array[Byte])
case class CaseClassBytes(classBytes: ClassBytes, moduleBytes: ModuleBytes)

object BytecodeGenerator {

  def dump(classData: ClassData): CaseClassBytes = {

    val name = classData.className.name

    val namespace: Option[String] = {
      if (classData.classNamespace.namespace.isDefined) Some(classData.classNamespace.namespace.get.replaceAllLiterally(".", "/"))
      else None
    }

    val caseClassName = {
      if (namespace.isDefined) namespace.get + "/" + name
      else name
    }

    val enrichedFields: List[EnrichedField] = classData.classFields.fields.map(field => FieldMatcher.enrichFieldData(namespace, field))

    val potentialNamespace = {
      if (classData.classNamespace.namespace.isDefined) classData.classNamespace.namespace.get
      else "<empty>"
    }
    
    val fieldsAsStrings = enrichedFields.map(f => (f.fieldName, f.fieldType.toString.replaceAll((potentialNamespace + "."), "").replaceAll(("scala."), "").replaceAll(("java.lang."), "")))

    val mySig = new ScalaSig(List("case class"), List(potentialNamespace, classData.className.name), fieldsAsStrings)

    //generate a pair of class and module class ($Dump is ASM's convention for naming the module class)
    val classBytes = ClassBytes(MyRecordDumper.dump(mySig, caseClassName, enrichedFields))
    val moduleBytes = ModuleBytes(MyRecord$Dumper.dump(caseClassName, enrichedFields))

    CaseClassBytes(classBytes, moduleBytes)

  }

}

