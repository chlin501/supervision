package supervision

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import scala.util.Try

trait Setting[T] {

  def get[A](key: String, default: A): A

  def set[A](key: String, value: A): Setting[T]

}

object Setting {

  def apply[T: Setting](): Setting[T] = implicitly[Setting[T]]

  protected[supervision] case class Typesafe(config: Config)
      extends Setting[Config] {

    override def get[A](key: String, default: A): A =
      Try(config.getAnyRef(key).asInstanceOf[A]).toOption match {
        case Some(value) => value
        case None        => default
      }

    override def set[A](key: String, default: A): Setting[Config] =
      Typesafe(config.withValue(key, ConfigValueFactory.fromAnyRef(default)))

  }

  implicit val typesafe = Typesafe(ConfigFactory.load("supervision"))
}
