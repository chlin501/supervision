package supervision

import org.slf4j.{LoggerFactory, Logger => Slf4jLogger}

trait Logging[T] {

  def info(message: => String)

  def debug(message: => String)

  def warn(message: => String)

  def trace(message: => String)

  def error(message: => String)

}
object Logging {

  def apply[T: Logging]() = implicitly[Logging[T]]

  implicit val slf4j = new Logging[Slf4jLogger] {

    protected[supervision] val log = LoggerFactory.getLogger(getClass)

    override def info(message: => String) =
      if (log.isInfoEnabled)
        log.info(message)

    override def debug(message: => String) =
      if (log.isDebugEnabled)
        log.debug(message)

    override def warn(message: => String) =
      if (log.isWarnEnabled)
        log.warn(message)

    override def trace(message: => String) =
      if (log.isTraceEnabled)
        log.trace(message)

    override def error(message: => String) =
      if (log.isErrorEnabled)
        log.error(message)

  }

}
