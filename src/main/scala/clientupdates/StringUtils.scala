package clientupdates

import java.text.SimpleDateFormat

import scala.util.{Success, Try}

object StringUtils {
  private val formats =
    List(
      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
      "yyyy-MM-dd'T'HH:mm:ss'Z'")
      .map(format => new SimpleDateFormat(format))

  def toUnixTime(str: String): Option[Int] = {
    val retFormat = formats
      .map { format => Try{format.parse(str)} }
      .collectFirst{ case Success(parsed) => parsed }
      .map { _.getTime / 1000 }
      .map { _.toInt }
      .headOption

    retFormat match {
      case Some(value) => {
        if (value < 0) {
          println(value + " caused by " + str)
        }
      }
      case None =>
    }
    retFormat
  }

  def toDouble(str: String): Option[Double] =
    Try { str.toDouble }.toOption
}
