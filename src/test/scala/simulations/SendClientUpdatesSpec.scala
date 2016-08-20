package simulations

import clientupdates.Conf
import org.scalatest._

class SendClientUpdatesSpec extends FlatSpec with Matchers {
  val noUpdates = new SendClientUpdates(new Conf(Nil))(List.empty)

  "update" should "should send with HTTP-Post" in {
    noUpdates.request.commonAttributes.method should be("POST")
  }
}