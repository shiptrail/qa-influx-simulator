package clientupdates

import java.io.{FileInputStream, InputStream}

import com.scalawilliam.xs4s.elementprocessor.XmlStreamElementProcessor

case class GpxTrackPoint(lat: Double, lng: Double, ele: Double, time: Int)

object GpxParser {
  def parse(fileName: String): Iterator[GpxTrackPoint] = {
    val fileInputStream = new FileInputStream(fileName)
    parse(fileInputStream)
  }

  def parse(is: InputStream): Iterator[GpxTrackPoint] = {
    val trkptSplitter = XmlStreamElementProcessor.collectElements(_.last == "trkpt")

    val trkpts = {
      import XmlStreamElementProcessor.IteratorCreator._
      val trkptStream = trkptSplitter.processInputStream(is)
      trkptStream
    }

    trkpts.map(xmlNode => {
      val lat = (xmlNode \ "@lat").text ifEmpty "-100.0"
      val lng = (xmlNode \ "@lon").text ifEmpty "-200.0"
      val time = (xmlNode \ "time").text ifEmpty "1969-01-01T01:00:00.000Z"
      val ele = (xmlNode \ "ele").text ifEmpty "-10000000.0"
      GpxTrackPoint(lat.toDouble, lng.toDouble, ele.toDouble, time.toUnixTime)
    })
  }

  implicit def stringToBetterString(string: String): BetterString = new BetterString(string)
}


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
