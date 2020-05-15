package supervision

import org.scalatest._
import zio._

class ServiceSpec extends FlatSpec with Logging {

  "Service" should "run and return value" in {
    import Service._
    val incByOne = (int: Int) => int + 1
    val runtime = Runtime.default
    val result = for {
      task <- zioRun(3)(incByOne).fork
      value <- task.join
    } yield value
    assertResult(runtime.unsafeRun(result))(4)
  }
}
