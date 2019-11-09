package supervision

import org.slf4j.LoggerFactory

trait Logging {
  protected[supervision] val log = LoggerFactory.getLogger(getClass)
}
