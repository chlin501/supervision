package supervision

class IdentifierSpec extends TestSpec {

  "Identifier" should "produce the same id" in {
    val name1 = "name1"
    val id1 = Identifier.create(name1)
    val id2 = Identifier.create("name1")
    log.info(s"id1 $id1, id2 $id2")
    assertResult(id1)(id2)
  }
}
