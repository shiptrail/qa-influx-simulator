import java.util.UUID

import clientupdates._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import play.api.libs.json._

import scala.concurrent.duration._

class SendFileBasedClientUpdates extends Simulation {
  val baseUrl: String = sys.props.getOrElse("urlPrefix", "http://localhost:9000/v2")
  val numClients: Int = sys.props.getOrElse("numClients", "1").toInt
  val sendInterval: Duration = sys.props.getOrElse("sendInterval", "1").toInt.millis
  val fileName: String = sys.props.getOrElse("fileName", "")
  val batchSize: Int = sys.props.getOrElse("batchSize", "1").toInt
  val endPoint = "send"

  val httpConf = http
    .baseURL(baseUrl) // Here is the root for all relative URLs
    .contentTypeHeader("application/json")

  val clientUpdates: Seq[ClientUpdate] = MultiFormatParser.parse(fileName).map {
    trackPoint =>
      ClientUpdate(trackPoint.lat, trackPoint.lng, trackPoint.ele, 0, trackPoint.time)
  }.toList

  lazy val chain = exec(session => {
    session.set("currentBatch", Json.toJson(session("updates").as[Iterator[ClientUpdate]].take(batchSize).toList))
  })
    .exec(http("request")
      .post("${endPointWithId}").body(StringBody("${currentBatch}")))

  val scn = scenario("Send location updates")
    .exec(session => {
      val id = UUID.randomUUID()
      val updates = clientUpdates.toIterator
      session
        .set("id", id)
        .set("endPointWithId", s"$baseUrl/$id/$endPoint")
        .set("updates", updates)
    })
    .repeat(Math.ceil(clientUpdates.length.toDouble / batchSize).toInt) {
      chain.pause(sendInterval)
    }

  setUp(scn.inject(atOnceUsers(numClients)).protocols(httpConf))
}

