package clientupdates

import java.io.{FileInputStream, InputStream}
import java.text.SimpleDateFormat

import com.scalawilliam.xs4s.elementprocessor.XmlStreamElementProcessor

import scala.util.{Success, Try}
import scala.xml.NodeSeq
import scala.language.implicitConversions

case class GpxTrackPoint(lat: Double, lng: Double, ele: Double, time: Int)

object GpxParser {
  def parse(fileName: String): Iterator[GpxTrackPoint] = {
    val fileInputStream = new FileInputStream(fileName)
    parse(fileInputStream)
  }

  def parse(is: InputStream): Iterator[GpxTrackPoint] = {
    val trkptSplitter = XmlStreamElementProcessor.collectElements(_.lastOption.contains("trkpt"))

    val trkpts = {
      import XmlStreamElementProcessor.IteratorCreator._
      trkptSplitter.processInputStream(is)
    }

    trkpts.flatMap { xmlNode =>
      for {
        lat <- whenDefined(xmlNode \ "@lat").flatMap(StringUtils.toDouble)
        lng <- whenDefined(xmlNode \ "@lon").flatMap(StringUtils.toDouble)
        time <- whenDefined(xmlNode \ "time").flatMap(StringUtils.toUnixTime)
        ele = whenDefined(xmlNode \ "ele").flatMap(StringUtils.toDouble).getOrElse(0.0)
      } yield GpxTrackPoint(lat, lng, ele, time)
    }
  }

  def whenDefined(parameter: NodeSeq): Option[String] = {
    val text: String = parameter.text
    if (text.isEmpty) {
      None
    } else {
      Some(text)
    }
  }
}
