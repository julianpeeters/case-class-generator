package avocet

import java.io._


class CaseClassGenerator(infile: File) {




  val parser = infile.getName.dropWhile(c => c != '.') match {
    case ".avro" => new AvroDatafileParser(infile)
    //TODO
    //case ".avsc" =>
    //case ".avpr" => 
    //case ".csv"  => 
    case _ => error("unsupported type")
  }
  val typeTemplate = parser.typeTemplate

}






