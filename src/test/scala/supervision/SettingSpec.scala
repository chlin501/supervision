package supervision

import org.scalatest._
import Logging._

class SettingSpec extends FlatSpec {

  val log = Logging()

  "Setting" should "get set value" in {
    import Setting._
    val config = Setting()
    val queueSize = config.get("supervision.supervisor.queue.size", 32)
    assertResult(32)(queueSize)
    val newConfig = config.set("a.b.c", 2.3d)
    val value = newConfig.get("a.b.c", Double.MinValue)
    assertResult(2.3d)(value)
  }
}
