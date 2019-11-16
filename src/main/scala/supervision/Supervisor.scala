package supervision

import zio.{Queue, Task, ZIO}

import scala.util.Random

trait Supervisor[S] {

  def id(): Identifier

  def name(): String

  def state(): Supervisor.State

  def withService(service: S): Supervisor[S]

  def start(): Task[Supervisor[S]]
}

object Supervisor {

  case class SupervisorTerminated(terminated: Terminated) extends Throwable {
    def id(): Identifier = terminated.id
  }

  protected[supervision] sealed trait State
  protected[supervision] case object Stopped extends State
  protected[supervision] case object Running extends State
  protected[supervision] case object Paused extends State
  protected[supervision] case class Terminated(id: Identifier) extends State

  protected[supervision] case class InternalQueue[T](
    queue: Task[Queue[T]] = Queue.unbounded[T]
  ) {

    def runServices(f: Queue[T] => Unit): InternalQueue[T] = InternalQueue(for {
      q <- queue
      _ <- Task(f(q)).fork
    } yield q)

    def enqueue(data: T): InternalQueue[T] = InternalQueue(for {
      q <- queue
      _ <- q.offer(data)
    } yield q)

    def dequeue(): Task[T] = for {
      q <- queue
      v <- q.take
    } yield v

  }

  def apply[S <: Service[_]](
    supervisorName: String = Random.alphanumeric.take(10).mkString(""),
    spec: Spec = Spec(),
    supervisorState: State = Stopped,
    services: Map[Identifier, S] = Map.empty[Identifier, S],
    offlineServices: Map[Identifier, S] = Map.empty[Identifier, S],
    restartQueue: InternalQueue[Identifier] = InternalQueue[Identifier]()
  )(implicit implicitInstance: (
    String,
    Spec,
    State,
    Map[Identifier, S],
    Map[Identifier, S],
    InternalQueue[Identifier]
  ) => Supervisor[S]): Supervisor[S] = implicitInstance (
    supervisorName,
    spec,
    supervisorState,
    services,
    offlineServices,
    restartQueue
  )

  implicit def supervisor[S <: Service[_]]: (
    String,
    Spec,
    State,
    Map[Identifier, S],
    Map[Identifier, S],
    InternalQueue[Identifier]
  ) => Supervisor[S] = (
    supervisorName: String,
    spec: Spec,
    supervisorState: State,
    services: Map[Identifier, S],
    offlineServices: Map[Identifier, S],
    restartQueue: InternalQueue[Identifier]
  ) => new Supervisor[S] {

    override def id(): Identifier = Identifier.create(name())

    override def name(): String = supervisorName

    override def state(): Supervisor.State = supervisorState

    override def withService(service: S): Supervisor[S] =
      supervisor[S](
        supervisorName,
        spec,
        supervisorState,
        services ++ Map(service.id -> service),
        offlineServices,
        restartQueue.enqueue(service.id)
      )

    def start(): Task[Supervisor[S]] = supervisorState match {
      case Stopped => Task(supervisor[S](
        supervisorName,
        spec,
        Running,
        services.map { case (id, service) =>
          service.start()
          (id, service)
        },
        offlineServices,
        restartQueue.runServices { queue => queue.map { id =>
          services.get(id).map(_.start)
        }}
      ))
      case Running => Task(this)
      case Terminated(id) => ZIO.fail(SupervisorTerminated(Terminated(id)))
      case Paused => Task(this) // TODO:
    }
  }
}
