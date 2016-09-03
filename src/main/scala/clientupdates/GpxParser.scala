package clientupdates

import java.io.InputStream

import scala.language.implicitConversions
import scala.xml.{Elem, NodeSeq}

object GpxParser extends GpsTrackParser with XmlBasedParser {
  def parse(is: InputStream): Iterator[TrackPoint] = {
    class ChildSelectable(ns: NodeSeq) {
      def \* = ns flatMap {
        _ match {
          case e: Elem => e.child
          case _ => NodeSeq.Empty
        }
      }
    }

    implicit def nodeSeqIsChildSelectable(xml: NodeSeq) = new ChildSelectable(xml)

    val nodes = getElementsWithNameFromInputStream("trkpt", is)
    //println(nodes.toList)

    for {
      xmlNode <- nodes
      lat <- whenDefined(xmlNode \ "@lat").flatMap(StringUtils.toDouble)
      lng <- whenDefined(xmlNode \ "@lon").flatMap(StringUtils.toDouble)
      time <- whenDefined(xmlNode \ "time").flatMap(StringUtils.toUnixTime)
      ele = whenDefined(xmlNode \ "ele").flatMap(StringUtils.toDouble).getOrElse(0.0)
      accelerometer <- whenDefinedNoneEmptySeq(xmlNode \ "extensions" \ "AccelerationExtension" \ "accel")
        .map {
          node => for {
            accelEntry <- node
            x <- whenDefined(accelEntry \ "@x").flatMap(StringUtils.toDouble)
            y <- whenDefined(accelEntry \ "@y").flatMap(StringUtils.toDouble)
            z <- whenDefined(accelEntry \ "@z").flatMap(StringUtils.toDouble)
            offset <- whenDefined(accelEntry \ "@offset").flatMap(StringUtils.toInt)
          } yield Accelerometer(x, y, z, offset)
        }
    } yield TrackPoint(lat, lng, ele, time, accelerometer)
  }
}
