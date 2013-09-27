package avocet

import java.io._
import models._

object CaseClassGenerator {
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
*/
  def make(classSeed: ClassData) = {
    new DynamicCaseClass(classSeed).instantiated$
  }
}






