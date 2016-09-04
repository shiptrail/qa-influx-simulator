package clientupdates

import java.io.InputStream

import clientupdates.TrackPoint.Accelerometer

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
      accel = (xmlNode \ "extensions" \ "AccelerationExtension" \ "accel")
        .flatMap {
          accelEntry => for {
            x <- whenDefined(accelEntry \ "@x").flatMap(StringUtils.toDouble)
            y <- whenDefined(accelEntry \ "@y").flatMap(StringUtils.toDouble)
            z <- whenDefined(accelEntry \ "@z").flatMap(StringUtils.toDouble)
            offset <- whenDefined(accelEntry \ "@offset").flatMap(StringUtils.toInt)
          } yield Accelerometer(x, y, z, offset)
        }
    } yield TrackPoint(lat, lng, time, Some(ele), accelerometer = accel)
  }
}
