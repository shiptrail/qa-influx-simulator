package simulations

import java.util.UUID

import clientupdates.ClientUpdate
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.scalacheck._
import play.api.libs.json._

import scala.concurrent.duration._

class SendRandomClientUpdates extends Simulation {
  val baseUrl: String = sys.props.getOrElse("urlPrefix", "http://localhost:9000/v2")
  val numClients: Int = sys.props.getOrElse("numClients", "1").toInt
  val sendInterval: Duration = sys.props.getOrElse("sendInterval", "1").toInt.millis
  val batchSize: Int = sys.props.getOrElse("batchSize", "10").toInt
  val endPoint = "send"
  val randomTrackListLength = 500


  val httpConf = http
    .baseURL(baseUrl) // Here is the root for all relative URLs
    .contentTypeHeader("application/json")

  val genClientUpdate = for {
    lat <- Gen.choose[Double](-90.0, 90.0)
    lng <- Gen.choose[Double](-180.0, 180.0)
    ele <- Gen.choose[Double](-100.0, 200.0)
    heading <- Gen.choose[Double](0.0, 360.0)
    timestamp <- Gen.choose[Int](0, 9999999)
  } yield ClientUpdate(lat, lng, ele, heading, timestamp)

  def genClientUpdates: List[ClientUpdate] = Gen.listOfN(randomTrackListLength, genClientUpdate).sample.get

  lazy val chain = exec(session => {
    session.set("currentBatch", Json.toJson(session("updates").as[Iterator[ClientUpdate]].take(batchSize).toList))
  })
    .exec(http("request")
      .post("${endPointWithId}").body(StringBody("${currentBatch}")))

  val scn = scenario("Send location updates")
    .exec(session => {
      val id = UUID.randomUUID()
      val updates = genClientUpdates.toIterator
      session
        .set("id", id)
        .set("endPointWithId", s"$baseUrl/$id/$endPoint")
        .set("updates", updates)
    })
    .repeat(Math.ceil(randomTrackListLength.toDouble / batchSize).toInt) {
      chain.pause(sendInterval)
    }

  setUp(scn.inject(atOnceUsers(numClients)).protocols(httpConf))
}

