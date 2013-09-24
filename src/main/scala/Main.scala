//From a Gist courtesy Eugene Burmako: https://gist.github.com/5845539.git
import scala.reflect.runtime.{universe => ru}
import scala.tools.reflect.ToolBox
import scala.language.reflectiveCalls


import scala.tools.scalap.scalax.rules.scalasig._

import com.novus.salat._
import com.novus.salat.global._

import scala.reflect.ScalaSignature
import reflect.internal.pickling.ByteCodecs
import reflect.internal.pickling.PickleFormat
import reflect.internal.pickling.PickleBuffer
import java.io._
import com.gensler.scalavro.types.AvroType
import com.gensler.scalavro.io.AvroTypeIO
import scala.util.{Try, Success, Failure}



object Test extends App {
  def define(tb: ToolBox[ru.type], tree: ru.ImplDef): ru.Symbol = {
    val compiler = tb.asInstanceOf[{ def compiler: scala.tools.nsc.Global }].compiler
    val importer = compiler.mkImporter(ru)
    val exporter = importer.reverse
    val ctree: compiler.ImplDef = importer.importTree(tree).asInstanceOf[compiler.ImplDef]
    def defineInternal(ctree: compiler.ImplDef): compiler.Symbol = {
      import compiler._
 
      val packageName = newTermName("__wrapper$" + java.util.UUID.randomUUID.toString.replace("-", ""))
      val pdef = PackageDef(Ident(packageName), List(ctree))
      val unit = new CompilationUnit(scala.reflect.internal.util.NoSourceFile)
      unit.body = pdef
 
      val run = new Run
      reporter.reset()
      run.compileUnits(List(unit), run.namerPhase)
      compiler.asInstanceOf[{ def throwIfErrors(): Unit }].throwIfErrors()
 
      ctree.symbol
    }
    val csym: compiler.Symbol = defineInternal(ctree)
    val usym = exporter.importSymbol(csym)
    usym
  }
 
  import scala.reflect.runtime.universe._
  import Flag._
  import scala.reflect.runtime.{currentMirror => cm}
 
  def pendingSuperCall() = Apply(Select(Super(This(tpnme.EMPTY), tpnme.EMPTY), nme.CONSTRUCTOR), Nil)
  // equivalent to q"class C"
  def cdef() = ClassDef(
    NoMods, newTypeName("C"), Nil,
    Template(
      List(Select(Ident(newTermName("scala")), newTypeName("AnyRef"))),
      emptyValDef,
      List(
        DefDef(NoMods, nme.CONSTRUCTOR, Nil, List(Nil), TypeTree(), Block(List(pendingSuperCall()), Literal(Constant(())))),
        DefDef(Modifiers(OVERRIDE), newTermName("toString"), Nil, Nil, TypeTree(), Literal(Constant("C")))
    )))
  def newc(csym: Symbol) = Apply(Select(New(Ident(csym)), nme.CONSTRUCTOR), List())
 
  val tb = cm.mkToolBox()
  val csym = define(tb, cdef())
  val obj = tb.eval(newc(csym))
//End Gist


  val cls = obj.getClass()
println(cls)

type Obj = cls.type
////println(typeOf[cl].typeSymbol) //type mismatch, found: Test.obj.type (with underlying type Any), req: AnyRef, can't cast
println(obj)
val scalaSig = ScalaSigParser.parse(cls)
  println(scalaSig)//none



val name = "MyRecord"
val memberName = "x"
val memberType = "String"


val parsed = tb.parse("""case class MyRecord(x: String)""")
//println("p: " + parsed)
val classs = tb.eval(parsed)//+ name + "(" + memberName + ": " + memberType + ")"))
////println(classs)

val S = tb.typeCheck(newc(csym)).tpe
//println(S)
type Record = S.type with CaseClass


//val cmm = cm.classSymbol(classs)
////println(cmm)
val T = tb.typeCheck(parsed).tpe
//println(T)
type MyRecord = T.type with CaseClass
//println(typeOf[Record].member(nme.CONSTRUCTOR)) //Constructor available for normal case class, but not my dumb parsed. how about eugene's?

//val dbo = grater[Record].asDBObject(obj)//dynamic types still have incorrect underlying type of Any

val scalaSig2 = ScalaSigParser.parse(cls)
println(scalaSig2)




 // val buf = new PickleBuffer(bytes, 0, bytes.length)




//T.getAnnotations.foreach(//println)
////println(an)
//an.foreach(//println)




//writing out with Pickler.pickle
val file = new File("outfile.txt")
val pw = new PrintWriter(file)
pw.write("squirrels are eating my plums")
pw.close



/*
val sc = cls.getAnnotation(classOf[ScalaSignature])
val sag = cls.getAnnotation(classOf[ScalaSignature])
//println(sc)


val bytes = sc.bytes.getBytes("UTF-8")
     // val len = ByteCodecs.decode(bytes)
 //     val bytecode = ByteCode(bytes.take(len))
//val atts = ScalaSigAttributeParsers.parse(bytecode)
////println(atts)

//val bt = ByteCode.forClass(S.getClass)
//val pp = ClassFileParser.parse(bt)
////println(pp)
*/



val sym: ClassSymbol = ru.runtimeMirror(cls.getClassLoader).classSymbol(cls)
println(typeOf[MyRecord])
////println(sym)
////println(sym.name)
////println(sym.owner)
////println(sym.privateWithin)
////println(sym.flagSets)

  val myRecordType = AvroType[MyRecord]
   // println("schema: " + myRecordType.schema)
}

