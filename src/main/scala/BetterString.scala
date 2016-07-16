package clientupdates

class BetterString(str: String) {
  def ifEmpty(replacement: String): String = if (str.length == 0) replacement else str

  def toUnixTime(): Int = {
    val parsedTime = try {
      val format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
      format.parse(str).getTime() / 1000
    } catch {
      case _:Throwable => {
        try {
          val format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
          format.parse(str).getTime() / 1000
        } catch {
          case _:Throwable => -1
        }
      }
    }
    parsedTime.toInt
  }
}