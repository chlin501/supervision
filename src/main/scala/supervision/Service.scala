package supervision

import zio._

trait Service[T[_]] {

  import supervision._

  def run(): ServiceResult[_]

}
case class ZioService[I, O](in: I, f: I => O) extends Service[Task] {

  import supervision._

  protected[supervision] val task = Task(f(in))

  override def run(): ServiceResult[O] =
    for {
      t <- task.fork
      value <- t.join
    } yield value

}
