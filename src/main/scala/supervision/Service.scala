package supervision

trait Service[T] {

  def id(): Identifier

  def name(): String

  def start(): Either[Throwable, Service[T]]

  def stop()
}

object Service {

  def apply[T](implicit s: Service[T]): Service[T] = s

}
