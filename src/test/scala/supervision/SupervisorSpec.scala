package supervision

import org.scalatest._

class SupervisorSpec extends FlatSpec with Logging {

  "Default Supervisor" should "match default values" in {

    trait MyService extends Service[String]

    val supervisor = Supervisor[MyService](supervisorName = "TestSupervisor")
    log.info(s"Supervisor id: ${supervisor.id()}")
    log.info(s"Supervisor name: ${supervisor.name()}")
    assertResult("TestSupervisor")(supervisor.name())
    log.info(s"Supervisor state: ${supervisor.state()}")
    assertResult(Supervisor.Stopped)(supervisor.state())
  }

}
