package clientupdates

import java.io.InputStream

import scala.language.implicitConversions

object GpxParser extends GpsTrackParser with XmlBasedParser {
  def parse(is: InputStream): Iterator[TrackPoint] = {
    val nodes = getElementsWithNameFromInputStream("trkpt", is)
    for {
      xmlNode <- nodes
      lat <- whenDefined(xmlNode \ "@lat").flatMap(StringUtils.toDouble)
      lng <- whenDefined(xmlNode \ "@lon").flatMap(StringUtils.toDouble)
      time <- whenDefined(xmlNode \ "time").flatMap(StringUtils.toUnixTime)
      ele = whenDefined(xmlNode \ "ele").flatMap(StringUtils.toDouble).getOrElse(0.0)
    } yield TrackPoint(lat, lng, ele, time)
  }
}
