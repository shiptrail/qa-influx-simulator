package clientupdates

import java.io.InputStream

object TcxParser extends GpsTrackParser with XmlBasedParser {
  override def parse(is: InputStream): Iterator[TrackPoint] = {
    val nodes = getElementsWithNameFromInputStream("Trackpoint", is)
    for {
      xmlNode <- nodes
      lat <- whenDefined(xmlNode \ "Position" \ "LatitudeDegrees").flatMap(StringUtils.toDouble)
      lng <- whenDefined(xmlNode \ "Position" \ "LongitudeDegrees").flatMap(StringUtils.toDouble)
      time <- whenDefined(xmlNode \ "Time").flatMap(StringUtils.toUnixTime)
      ele = whenDefined(xmlNode \ "AltitudeMeters").flatMap(StringUtils.toDouble).getOrElse(0.0)
    } yield TrackPoint(lat, lng, ele, time, Seq.empty)
  }
}
