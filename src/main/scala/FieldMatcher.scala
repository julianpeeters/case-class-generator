package caseclass.generator
import org.objectweb.asm._
import Opcodes._



object FieldMatcher {

  def enrichFieldData(namespace: String, field: FieldSeed): FieldData = {

    val fieldType = field.fieldType

    FieldData( 
      field.fieldName, 
      fieldType, 
      getTypeData(namespace, fieldType) 
    )
  }

  def getTypeData(namespace: String, fieldType: String): TypeData = {

println("FieldMatcher getTypeData fieldType " + fieldType)
    fieldType match {

      case "Null"    => { 
        TypeData(
          Type.getDescriptor(classOf[Null]),
          "Ljava/lang/Object;",
          ALOAD,
          ARETURN,
          null.asInstanceOf[Object]
        )
      }
      case "Boolean" => {
        TypeData(
          Type.getDescriptor(classOf[Boolean]),
          "Ljava/lang/Object;",
          ILOAD,
          IRETURN,
          true.asInstanceOf[Object]
        )
      }
      case "Int"     => {
        TypeData(
          Type.getDescriptor(classOf[Int]),
          "Ljava/lang/Object;",
          ILOAD,
          IRETURN,
          1.asInstanceOf[Object]
        )
      }
      case "Long"    => {
        TypeData(Type.getDescriptor(classOf[Long]),
          "Ljava/lang/Object;",
          LLOAD,
          LRETURN, 
          1L.asInstanceOf[Object]
        )
      }
      case "Float"   => {
        TypeData(
          Type.getDescriptor(classOf[Float]),
          "Ljava/lang/Object;",
          FLOAD,
          FRETURN,
          1F.asInstanceOf[Object]
        )
      }
      case "Double"  => {
        TypeData(
          Type.getDescriptor(classOf[Double]),
          "Ljava/lang/Object;",
          DLOAD,
          DRETURN,
          1D.asInstanceOf[Object]
        )
      }
      case "String"  => {
        TypeData(
          Type.getDescriptor(classOf[String]),
          "Ljava/lang/String;",
          ALOAD,
          ARETURN,
          ""
        )
      }
      case "Byte"    => {
        TypeData(
          Type.getDescriptor(classOf[Byte]),
          "Ljava/lang/Object;",
          ILOAD,
          IRETURN,
          1.toByte.asInstanceOf[Object]
        )
      }
      case "Short"    => {
        TypeData(
          Type.getDescriptor(classOf[Short]),
          "Ljava/lang/Object;",
          ILOAD,
          IRETURN,
          1.toShort.asInstanceOf[Object]
        )
      }
      case "Char"    => {
        TypeData(
          Type.getDescriptor(classOf[Char]),
          "Ljava/lang/Object;",
          ILOAD,
          IRETURN,
          'k'.asInstanceOf[Object]
        )
      }
      case "Any"    => {
        TypeData(
          Type.getDescriptor(classOf[Any]),
          "Ljava/lang/Object;",
          ALOAD,
          ARETURN,
          "".asInstanceOf[Any].asInstanceOf[Object]
        )
      }
      case "AnyRef"    => {
        TypeData(
          Type.getDescriptor(classOf[AnyRef]),
          "Ljava/lang/Object;",
          ALOAD,
          ARETURN,
          "".asInstanceOf[AnyRef].asInstanceOf[Object]
        )
      }
      case "Unit"    => {
        TypeData(
          "Lscala/runtime/BoxedUnit;",//Type.getDescriptor(classOf[Unit])
          "Ljava/lang/Object;",
          ALOAD,
          RETURN,
          ().asInstanceOf[scala.runtime.BoxedUnit]
        )
      }
      case "Nothing"    => {
        TypeData(
          Type.getDescriptor(classOf[Nothing]),
          "Ljava/lang/Object;",
          ALOAD,
          ARETURN,
          null//here's a conundrum, there is no instance of Nothing
        )
      }
      case "Object"    => {
        TypeData(
          Type.getDescriptor(classOf[Object]),
          "Ljava/lang/Object;",
          ALOAD,
          ARETURN,
          new Object
        )
      }

      //Complex 

      //User-Defined
      case name: String => {
       CaseClassGenerator.generatedClasses.foreach(println)
        TypeData(
          "L"+ name + ";", //if its a string but none of the above, its a nested record type
          "Ljava/lang/Object;",//not at all sure that this is the correct thing to use here
          ALOAD,
          ARETURN,
//          CaseClassGenerator.generatedClasses.get(namespace + "." + name).get.instantiated$.asInstanceOf[Object]
          CaseClassGenerator.generatedClasses.get(name).get.instantiated$.asInstanceOf[Object]
        )
      }
      case _         => error("only Strings are valid type names")
    }
  }

/*
The ILOAD, LLOAD, FLOAD, DLOAD, and ALOAD instructions read a local variable
and push its value on the operand stack. They take as argument the index
i of the local variable that must be read. ILOAD is used to load a boolean,
byte, char, short, or int local variable. LLOAD, FLOAD and DLOAD are used to
load a long, float or double value, respectively (LLOAD and DLOAD actually
load the two slots i and i+ 1). Finally ALOAD is used to load any non primitive
value, i.e. Object and array references.
*/

def getReturnTypes(fieldSeeds: List[FieldSeed]) = {
fieldSeeds.map(n => n.fieldType).map(m => m match {
      //    case "Null"    => classOf[Unit]
      case "Boolean" => classOf[Boolean]
      case "Int"     => classOf[Int]
      case "Long"    => classOf[Long]
      case "Float"   => classOf[Float]
      case "Double"  => classOf[Double]
      case "bytes"   => classOf[Seq[Byte]]
      case "String"  => classOf[String]
      //Complex ------------------------
      //case "record"  => (modelClass.toString, modelClass.toString)   //MyRecord-and-others simulataneously?-----Needs a test
      case "enum"    => classOf[Enumeration#Value]
      case "array"   => classOf[Seq[_]]
      case "map"     => classOf[Map[String, _]]
      case "Map(type -> record, name -> rec, doc -> , fields -> List(Map(name -> i, type -> List(int, null))))"     => classOf[Map[String, _]]
   

                         case "Short"   => classOf[Short]
                         case "Byte"    => classOf[Byte]
                         case "Char"    => classOf[Char]
                         case "Any"     => classOf[Any]
                         case "AnyRef"  => classOf[AnyRef]
                         case "Unit"    => classOf[scala.runtime.BoxedUnit]//classOf[Unit]
                         case "Nothing" => classOf[Nothing]
                         case "Null"    => classOf[Null]
                         case "Object"  => classOf[Object]
      //  case "option"   =>  classOf[Option[Any]]
      //     case n: List[Any] => classOf[Option[Any]]         
                         
      case x: String => x //if its a string but none of the above, its a nested record type
    //  case a:Any     => a

    })
}


}
