package supervision

import zio._

trait Service[T[_]] {

  def run[I, O](in: I)(f: I => O): T[O]

}

object Service {

  def apply[T[_]](implicit s: Service[T]): Service[T] = s

  def zioRun[I, O, S[_]: Service](in: I)(f: I => O) = Service[S].run(in)(f)

  implicit val zioService = new Service[Task] {

    override def run[I, O](in: I)(f: I => O): Task[O] = Task(f(in))

  }

}
