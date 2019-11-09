package supervision

import com.google.common.base.Charsets._
import com.google.common.hash.Hashing

object Identifier {

  protected[supervision] def hash(value: String): String = Hashing
    .sipHash24()
    .newHasher()
    .putString(value, UTF_8)
    .hash
    .toString

  def create(value: String): Identifier = Identifier(hash(value))

}
protected[supervision] final case class Identifier(value: String)
