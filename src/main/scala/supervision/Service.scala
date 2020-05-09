package supervision

import zio._
import supervision._

trait Service[T] {

  def id(): Identifier

  def start(): FR[T]

}

object Service {

  type AUX[T] = Task[T]

  protected[supervision] sealed trait State
  protected[supervision] case object Stopped extends State
  protected[supervision] case object Running extends State
  protected[supervision] case object Paused extends State
  protected[supervision] case class Failed(throwable: Throwable) extends State

  def apply[T](implicit s: Service[T]): Service[T] = s

  implicit def service[T](
      identifier: Identifier,
      task: AUX[T]
  ) =
    new Service[T] {

      override def id(): Identifier = identifier

      override def start(): FR[T] = task.fork

    }

}
