import com.gensler.scalavro.types.AvroType

object Main extends App{
println(AvroType[Seq[String]].schema)
}
