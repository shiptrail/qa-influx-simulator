package clientupdates
import java.io.InputStream

import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object CustomFormatParser extends GpsTrackParser {
  override def parse(inputStream: InputStream): Iterator[TrackPoint] = {
    val lines = Source.fromInputStream(inputStream).getLines()
    for {
      line: String <- lines
      trackPoint <- Json.parse(line).asOpt[TrackPoint]
    } yield trackPoint
  }
}
