import com.gensler.scalavro.types.AvroType
import com.gensler.scalavro.io.AvroTypeIO
import scala.util.{Try, Success, Failure}
import java.io._
import avocet._
import models._
import scala.reflect.runtime.universe


object Main extends App{

   val fieldData: List[FieldData] = List(
     FieldData("x","string","Ljava/lang/String;", "Ljava/lang/String;",25,176, "java.lang.String")
  )

  val typeTemplate = CaseClassGenerator.make(ClassData("models", "MyRecord", fieldData, List(classOf[String])))

  type MyRecord = typeTemplate.type

  val myRecordType = AvroType[MyRecord]
   // println("schema: " + myRecordType.schema)
/*
  val myRecord = MyRecord("hello Scalavro")

  val file = new File("data")
  val outStream: OutputStream = new FileOutputStream(new File("data"))
  myRecordType.io.write(myRecord, outStream)

  val inStream: InputStream = new FileInputStream(file)
  myRecordType.io.read(inStream) match {
    case Success(readResult) => println("successfully deserialized: " + readResult)//readResult is instance of MyRecord
    case Failure(cause)      => // handle failure...
  }
*/
}
