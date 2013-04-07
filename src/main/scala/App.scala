import models._
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._

object Main extends App {
  val hello = MyRecord("helloworld")
  val dbo = grater[MyRecord].asDBObject(hello)
  println(dbo)
}
