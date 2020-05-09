package supervision

import zio._

package object supervision {

  type S[A] = Service[A]

  type FR[A] = URIO[Any, Fiber.Runtime[Throwable, A]]

}
