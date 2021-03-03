package supervision

import org.scalatest._
import Logging._
import zio._

class TestSpec extends FlatSpec {

  protected[supervision] val log = Logging()

  protected[supervision] val runtime = Runtime.default

  def runSync[E, A](result: ZIO[Any, E, A]) = runtime.unsafeRun(result)

}
