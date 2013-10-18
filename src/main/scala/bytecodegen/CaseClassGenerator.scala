//package avocet

//import java.io._
//import models._

package models
import avocet._
import org.objectweb.asm._
import Opcodes._

class CaseClassGenerator {
/*
  def parseFromFile(infile: File) = infile.getName.dropWhile(c => c != '.') match {
    case ".avro" => {
      new AvroDatafileParser(infile).classSeeds
        .map(classSeed => new DynamicCaseClass(classSeed))
        .map(cls => cls.instantiated$)
        .head//get an instance of the top-level record
    }
    //TODO
    //case ".avsc" =>
    //case ".avpr" => 
    //case ".csv"  => 
    case _ => error("unsupported file format")
  }

  def make(classSeed: ClassData) = {
    new DynamicCaseClass(classSeed).instantiated$
  }

*/


def generateBytecode(classData: ClassData): List[Array[Byte]] = {
  val caseClassName = classData.classNamespace + "/" + classData.className
  val fieldData: List[FieldData] = classData.classFields.map(field => FieldMatcher.enrichFieldData(field) )
  val mySig = new ScalaSig(List("case class"), List(classData.classNamespace, classData.className), fieldData.map(f => (f.fieldName, f.fieldType.capitalize)))
//generate a pair of class and module class
  List(new MyRecordDump().dump(mySig, caseClassName, fieldData), new MyRecord$Dump().dump(caseClassName, fieldData) )//ASM's convention for naming the module class

  }

}






