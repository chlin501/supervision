package supervision

import zio._
import scala.util.Random

package object supervision {

  def randomString(size: Int = 10) = Random.alphanumeric.take(size).mkString("")

  implicit class StringOps(value: String) {

    def isEmpty: Boolean = null == value || "".equals(value)

  }

}
