package clientupdates

import java.io.InputStream

import com.scalawilliam.xs4s.elementprocessor.XmlStreamElementProcessor

object TcxParser extends GpsTrackParser  {

  override def parse(is: InputStream): Iterator[TrackPoint] = {
    val TrackpointSplitter = XmlStreamElementProcessor.collectElements(_.last == "Trackpoint")

    val trackpoints = {
      import XmlStreamElementProcessor.IteratorCreator._
      val trackpointStream = TrackpointSplitter.processInputStream(is)
      trackpointStream
    }
    trackpoints.map(xmlNode => {
      val lat = (xmlNode \ "Position" \ "LatitudeDegrees").text ifEmpty "-100.0"
      val lng = (xmlNode \ "Position" \ "LongitudeDegrees").text ifEmpty "-200.0"
      val time = (xmlNode \ "Time").text ifEmpty "1969-01-01T01:00:00.000Z"
      val ele = (xmlNode \ "AltitudeMeters").text ifEmpty "-10000000.0"
      TrackPoint(lat.toDouble, lng.toDouble, ele.toDouble, time.toUnixTime)
    })
  }

  implicit def stringToBetterString(string: String): BetterString = new BetterString(string)
}
