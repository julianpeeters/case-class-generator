package caseclass.generator
import artisinal.pickle.maker._
import org.objectweb.asm._
import Opcodes._

import java.io._
import scala.util.parsing.json._


object BytecodeGenerator {

  def dump(classData: ClassData): List[Array[Byte]] = {
    val name = classData.className
    val namespace = classData.classNamespace
    val caseClassName = namespace + "/" + name
    val fieldData: List[FieldData] = classData.classFields.map(field => FieldMatcher.enrichFieldData(namespace, field) )
println(fieldData)
    val mySig = new ScalaSig(List("case class"), List(classData.classNamespace, classData.className), fieldData.map(f => (f.fieldName, f.fieldType)))

    //generate a pair of class and module class
    List(new MyRecordDump().dump(mySig, caseClassName, fieldData),
       new MyRecord$Dump().dump(caseClassName, fieldData) )// $Dump is ASM's convention for naming the module class

  }

}




