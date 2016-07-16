package clientupdates

import java.io.InputStream

import com.scalawilliam.xs4s.elementprocessor.XmlStreamElementProcessor

object GpxParser extends GpsTrackParser {

  override def parse(is: InputStream): Iterator[TrackPoint] = {
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
      TrackPoint(lat.toDouble, lng.toDouble, ele.toDouble, time.toUnixTime)
    })
  }

  implicit def stringToBetterString(string: String): BetterString = new BetterString(string)
}
