package clientupdates

import java.io.ByteArrayInputStream

import org.scalatest._
import play.api.libs.json.Json

class CustomFormatParserSpec extends FlatSpec with Matchers {
  val trackpoints = List(
    TrackPoint(52.4323181994, 13.1817903835, 1470655045, Some(45.85)),
    TrackPoint(52.4323181156, 13.1817902997, 1470655046, Some(45.85))
  )

  val customFileFormat: String = trackpoints.map((tp) => Json.toJson(tp).toString()).mkString("\n")

  it should "be able to parse an InputStream containing cgps data" in {
    val parsedTracks = CustomFormatParser.parse(new ByteArrayInputStream(customFileFormat.getBytes()))

    parsedTracks.toList should be(trackpoints)
  }
}