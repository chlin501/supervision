package supervision

import java.io.Serializable

import zio._

trait Supervisor[T] {

  import supervision._

  /**
    * Add a task to Supervisor
    * @param in
    * @param f
    * @return
    */
  def withTask[I, O](in: I)(
      f: I => O
  ): ZIO[Any, Nothing, Supervisor[Service[Task]]]

  /**
    * Start Supervisor.
    *  TODO: This should calls zio.Task#fork
    */
  def start(): ServiceResult[_]

}
object Supervisor {

  def apply[T: Supervisor](): Supervisor[T] = implicitly[Supervisor[T]]

  implicit val supervisor = new Supervisor[Service[Task]] {

    import Setting._
    import supervision._

    protected[supervision] val config = Setting()

    protected[supervision] val queue = Queue.bounded[Service[Task]](
      requestedCapacity = config.get("supervision.supervisor.queue.size", 32)
    )

    override def withTask[I, O](
        in: I
    )(f: I => O): ZIO[Any, Nothing, Supervisor[Service[Task]]] =
      for {
        q <- queue
        _ <- q.offer(ZioService[I, O](in, f))
      } yield this

    override def start(): ServiceResult[_] =
      for {
        q <- queue
        service <- q.take
        value <- service.run()
      } yield value

  }

}
