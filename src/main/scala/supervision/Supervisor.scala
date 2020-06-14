package supervision

import java.io.Serializable

import zio._

trait Supervisor[T] {

  def withService(
      service: Service[Task]
  ): ZIO[Any, Nothing, Supervisor[Service[Task]]]

}
object Supervisor {

  def apply[T](implicit s: Supervisor[T]): Supervisor[T] = s

  implicit val supervisor = new Supervisor[Service[Task]] {

    import Setting._

    protected[supervision] val config = typesafe()

    protected[supervision] val queue = Queue.bounded[Service[Task]](
      requestedCapacity = config.get("supervision.supervisor.queue.size", 32)
    )

    def withService(
        service: Service[Task]
    ): ZIO[Any, Nothing, Supervisor[Service[Task]]] =
      for {
        q <- queue
        _ <- q.offer(service)
      } yield this

  }

}
