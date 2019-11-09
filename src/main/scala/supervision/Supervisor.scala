package supervision

import supervision._
import scala.collection.immutable.Queue

trait Supervisor[S] {

  def id(): Identifier

  def name(): String

  def withService(service: S): Supervisor[S]

  def start(): Either[Throwable, Supervisor[S]]
}

object Supervisor {

  case class SupervisorTerminated(terminated: Terminated) extends Throwable

  protected[supervision] sealed trait State
  protected[supervision] case object Stopped extends State
  protected[supervision] case object Running extends State
  protected[supervision] case object Paused extends State
  protected[supervision] case class Terminated(id: Identifier) extends State


  def apply[S <: Service[_]](
    value: String,
    spec: Spec = Spec(),
    state: State = Stopped,
    services: Map[Identifier, S] = Map.empty[Identifier, S],
    offlineServices: Map[Identifier, S] = Map.empty[Identifier, S],
    restartQeueu: Queue[Identifier] = Queue.empty[Identifier]
  )(
    implicit s: (String, Spec, State, Map[Identifier, S], Map[Identifier, S], Queue[Identifier]) => Supervisor[S]
  ): Supervisor[S] = s(value, spec, state, services, offlineServices, restartQeueu)

  implicit def supervisor[S <: Service[_]]:
    (String, Spec, State, Map[Identifier, S], Map[Identifier, S], Queue[Identifier] ) => Supervisor[S] =
  (
    value: String,
    spec: Spec,
    state: State,
    services: Map[Identifier, S],
    offlineServices: Map[Identifier, S],
    restartQeueu: Queue[Identifier]
  ) => new Supervisor[S] {

    override def id(): Identifier = Identifier.create(value)

    override def name(): String = "Supervisor"

    override def withService(service: S): Supervisor[S] =
      supervisor[S](
        value,
        spec,
        state,
        services ++ Map(service.id -> service),
        offlineServices,
        restartQeueu.enqueue(service.id)
      )

    def start(): Either[Throwable, Supervisor[S]] = state match {
      case Stopped => Right(supervisor[S](
        value,
        spec,
        state,
        services.map { case (id, service) =>
          service.start()
          (id, service)
        },
        offlineServices,
        restartQeueu
      ))
      case Running => Right(this)
      case Terminated(value) => Left(SupervisorTerminated(Terminated(value)))
      case Paused => Right(this) // TODO:
    }

  }

}
