package clientupdates

import java.io.{FileInputStream, InputStream}
import java.text.SimpleDateFormat

import com.scalawilliam.xs4s.elementprocessor.XmlStreamElementProcessor

import scala.util.Try
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
        lat <- whenDefined(xmlNode \ "@lat")
        lng <- whenDefined(xmlNode \ "@lon")
        time <- whenDefined(xmlNode \ "time")
        ele <- whenDefined(xmlNode \ "ele")
      } yield GpxTrackPoint(lat.toDouble, lng.toDouble, ele.toDouble, time.toUnixTime)
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

  implicit def stringToBetterString(string: String): BetterString = new BetterString(string)
}


class BetterString(str: String) {
  val formats = List("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss'Z'").map(format => new SimpleDateFormat(format))

  def toUnixTime(): Int = {
    formats
      .map { format => Try(format.parse(str)).toOption }
      .collectFirst{ case Some(parsed) => parsed }
      .map { _.getTime() / 1000 }
      .map { _.toInt }
      .getOrElse(-1)
  }
}
