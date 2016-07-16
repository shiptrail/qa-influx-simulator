import clientupdates.{ClientUpdate, GpxParser}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.scalacheck._
import play.api.libs.json._

import scala.concurrent.duration._
import scala.language.implicitConversions

class SendFileBasedClientUpdates extends Simulation {

  implicit def jsonValToString(jsonVal: JsValue): String = jsonVal.toString()

  val baseUrl: String = sys.props.getOrElse("urlPrefix", "http://localhost:9000/v1")
  val numClients: Int = sys.props.getOrElse("numClients", "1").toInt
  val sendInterval: Duration = sys.props.getOrElse("sendInterval", "1").toInt.millis
  val fileName: String = sys.props.getOrElse("fileName", "")
  val batchSize: Int = sys.props.getOrElse("batchSize", "1").toInt
  val endPoint = baseUrl + "/send"

  val httpConf = http
    .baseURL(baseUrl) // Here is the root for all relative URLs
    .contentTypeHeader("application/json")

  val clientUpdates: Seq[ClientUpdate] = GpxParser.parse(fileName).map {
    gpxTrackPoint =>
      ClientUpdate(1, gpxTrackPoint.lat, gpxTrackPoint.lng, gpxTrackPoint.ele, 0, gpxTrackPoint.time)
  }.toList

  lazy val chain = exec(session => {
    session.set("currentBatch", Json.toJson(session("updates").as[Iterator[ClientUpdate]].take(batchSize).toList))
  })
    .exec(http("request")
      .post(endPoint).body(StringBody("${currentBatch}")))

  val scn = scenario("Send location updates")
    .exec(session => {
      val id = Gen.posNum[Int].sample.get
      val updates = clientUpdates.toIterator.map( update => update.copy(id=id))
      session.set("id", id).set("updates", updates)
    })
    .repeat(Math.ceil(clientUpdates.length.toDouble / batchSize).toInt) {
      chain.pause(sendInterval)
    }

  setUp(scn.inject(atOnceUsers(numClients)).protocols(httpConf))
}

