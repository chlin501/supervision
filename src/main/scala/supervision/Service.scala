package supervision

trait Service[T] {

  def id(): Identifier

  def name(): String

  def state(): Service.State

  def start(): Either[Throwable, Service[T]]

  def stop()
}

object Service {

  protected[supervision] sealed trait State
  protected[supervision] case object Stopped extends State
  protected[supervision] case object Running extends State
  protected[supervision] case object Paused extends State
  protected[supervision] case class Failed(throwable: Throwable) extends State

  def apply[T](implicit s: Service[T]): Service[T] = s

}
