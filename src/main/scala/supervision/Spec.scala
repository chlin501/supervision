package supervision

import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt

protected[supervision] case class Spec (
  decay: Duration = 30 seconds,
  threshold: Int = 5,
  backoff: Duration = 15 seconds,
  timeout: Duration = 10 seconds
)
