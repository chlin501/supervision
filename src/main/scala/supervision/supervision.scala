package supervision

import zio._

package object supervision {

  type ServiceResult[O] = ZIO[Any, Throwable, O]

  implicit class StringOps(value: String) {

    def isEmpty: Boolean = null == value || "".equals(value)

  }

}
