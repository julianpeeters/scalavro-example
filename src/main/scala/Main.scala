//import com.gensler.scalavro.AvroType
import com.gensler.scalavro.types.AvroType
import com.gensler.scalavro.io.AvroTypeIO
import scala.util.{Try, Success, Failure}
import java.io._
import models._

object Main extends App{

  val myRecordType = AvroType[MyRecord]
    println("schema: " + myRecordType.schema)

  val myRecord = MyRecord("hello Scalavro")

  val file = new File("data")
  val outStream: OutputStream = new FileOutputStream(new File("data"))
  myRecordType.io.write(myRecord, outStream)

  val inStream: InputStream = new FileInputStream(file)
  myRecordType.io.read(inStream) match {
    case Success(readResult) => println("successfully deserialized: " + readResult)//readResult is instance of MyRecord
    case Failure(cause)      => // handle failure...
  }
}
