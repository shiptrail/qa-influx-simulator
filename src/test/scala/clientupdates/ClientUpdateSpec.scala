package clientupdates

import org.scalatest._
import play.api.libs.json.Json

class ClientUpdateSpec extends FlatSpec with Matchers {
  val caseClass = ClientUpdate(42.0, 42.0, 42.0, 42.0, 42, Seq.empty)

  val json = Json.obj(
    "lat" -> 42.0,
    "lng" -> 42.0,
    "ele" -> 42.0,
    "heading" -> 42.0,
    "timestamp" -> 42,
    "accelerometer" -> Json.arr()
  )

  "ClientUpdate" should "be able to transform a ClientUpdate case class into a clientUpdate JSON object " in {
    val clientUpdateCaseClass = caseClass
    val expectedJson = json

    val transformedJson = Json.toJson(clientUpdateCaseClass)
    transformedJson should be(expectedJson)
  }

  "ClientUpdate" should "be able to transform a clientUpdate JSON object into a ClientUpdate case class" in {
    val expectedCaseClass = caseClass
    val jsonForCaseClass = json

    val transformedCaseClass = jsonForCaseClass.as[ClientUpdate]
    transformedCaseClass should be(expectedCaseClass)
  }
}