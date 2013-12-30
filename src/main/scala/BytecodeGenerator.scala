package caseclass.generator
import artisanal.pickle.maker._
import org.objectweb.asm._
import Opcodes._

import java.io._
import scala.util.parsing.json._


object BytecodeGenerator {

  def dump(classData: ClassData): List[Array[Byte]] = {

    val name = classData.className

    val namespace : Option[String] = { 
      if (classData.classNamespace.isDefined) Some(classData.classNamespace.get.replaceAllLiterally(".", "/"))
      else None
    }

    val caseClassName = { 
      if (namespace.isDefined) namespace.get + "/" + name
      else name
    }

    val fieldData: List[FieldData] = classData.classFields.map(field => FieldMatcher.enrichFieldData(namespace, field) )

    val potentialNamespace = {
      if (classData.classNamespace.isDefined) classData.classNamespace.get
      else "<empty>"
    }

    val mySig = new ScalaSig(List("case class"), List(potentialNamespace, classData.className), fieldData.map(f => (f.fieldName, f.fieldType)))

    //generate a pair of class and module class
    List(new MyRecordDump().dump(mySig, caseClassName, fieldData),
         new MyRecord$Dump().dump(caseClassName, fieldData) )// $Dump is ASM's convention for naming the module class

  }

}




