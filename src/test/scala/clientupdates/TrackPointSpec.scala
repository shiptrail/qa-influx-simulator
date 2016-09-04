package clientupdates

import org.scalatest._
import play.api.libs.json.Json

class TrackPointSpec extends FlatSpec with Matchers {
  val caseClass = TrackPoint(42.0, 42.0, 42, Some(42.0))

  val json = Json.obj(
    "lat" -> 42.0,
    "lng" -> 42.0,
    "timestamp" -> 42,
    "ele" -> Some(42.0),
    "gpsMeta" -> Json.arr(),
    "compass" -> Json.arr(),
    "accelerometer" -> Json.arr(),
    "orientation" -> Json.arr(),
    "annotation" -> Json.arr()
  )

  "TrackPoint" should "be able to transform a TrackPoint case class into a TrackPoint JSON object " in {
    val TrackPointCaseClass = caseClass
    val expectedJson = json

    val transformedJson = Json.toJson(TrackPointCaseClass)
    transformedJson should be(expectedJson)
  }

  "TrackPoint" should "be able to transform a TrackPoint JSON object into a TrackPoint case class" in {
    val expectedCaseClass = caseClass
    val jsonForCaseClass = json

    val transformedCaseClass = jsonForCaseClass.as[TrackPoint]
    transformedCaseClass should be(expectedCaseClass)
  }
}