package clientupdates

import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.Json

class ClientUpdateSpec extends FlatSpec with Matchers with PropertyChecks {

  "The format" should "read its own output" in {
     Json.toJson(ClientUpdate(1, 1, 1, 1, 1, 1)).as[ClientUpdate] should be(ClientUpdate(1, 1, 1, 1, 1, 1))
  }

}
