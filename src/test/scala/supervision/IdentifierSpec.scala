package supervision

import org.scalatest._
import Logging._

class IdentifierSpec extends FlatSpec {

  val log = Logging()

  "Identifier" should "produce id deterministically" in {
    val name1 = "name1"
    val id1 = Identifier.create(name1)
    val id2 = Identifier.create("name1")
    log.info(s"id1 $id1, id2 $id2")
    assertResult(id1)(id2)
  }
}
