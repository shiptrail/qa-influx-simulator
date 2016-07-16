import clientupdates._
import org.scalatest._
import play.api.libs.json.Json

import scala.language.reflectiveCalls

class ClientUpdateSpec extends FlatSpec with Matchers {
  "ClientUpdate" should "be able to transform a clientUpdate JSON object into a ClientUpdate case class" in {
    val clientUpdateCaseClass = ClientUpdate(42, 42.0, 42.0, 42.0, 42.0, 42)

    val expectedJson = Json.obj(
      "id" -> 42,
      "lat" -> 42.0,
      "lng" -> 42.0,
      "ele" -> 42.0,
      "heading" -> 42.0,
      "timestamp" -> 42
    )
    val transformedJson = Json.toJson(clientUpdateCaseClass)
    transformedJson should be(expectedJson)
  }
}