package clientupdates

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.scalacheck._
import play.api.libs.json._

import scala.concurrent.duration._

class SendFileBasedClientUpdates extends Simulation {

  implicit def jsonValToString(jsonVal: JsValue): String = jsonVal.toString()

  val baseUrl: String = sys.props.getOrElse("urlPrefix", "http://localhost:9000/v1")
  val numClients: Int = sys.props.getOrElse("numClients", "1").toInt
  val sendInterval: Int = sys.props.getOrElse("sendInterval", "1").toInt
  val fileName: String = sys.props.getOrElse("fileName", "")
  val batchSize: Int = sys.props.getOrElse("batchSize", "1").toInt
  val endPoint = baseUrl + "/send"

  val httpConf = http
    .baseURL("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .contentTypeHeader("application/json")

  val clientUpdates: Seq[ClientUpdate] = MultiFormatParser.parse(fileName).map {
    trackPoint =>
      ClientUpdate(1, trackPoint.lat, trackPoint.lng, trackPoint.ele, 0, trackPoint.time)
  }.toList

  lazy val chain = exec(session => {
    session.set("currentBatch", Json.toJson(session("updates").as[Iterator[ClientUpdate]].take(batchSize).toList))
  })
    .exec(http("request")
      .post(endPoint).body(StringBody("${currentBatch}")))


  val scn = scenario("Send location updates")
    .exec(session => {
      val updates = clientUpdates.toIterator
      val id = Gen.posNum[Int].sample.get
      session.set("id", id).set("updates", updates.map( update => update.copy(id=id)))
    })
    .repeat(Math.ceil(clientUpdates.length / batchSize).asInstanceOf[Int]) {
      chain.pause(sendInterval.millis)
    }

  setUp(scn.inject(atOnceUsers(numClients)).protocols(httpConf))
}

