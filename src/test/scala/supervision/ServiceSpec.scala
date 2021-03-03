package supervision

class ServiceSpec extends TestSpec {

  "Service" should "run a function" in {
    val add = (int: Int) => int + 1
    val service = ZioService(2, add)
    val result = service.run()
    assertResult(3)(runSync(result))
  }
}
