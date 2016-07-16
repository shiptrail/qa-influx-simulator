import clientupdates.ClientUpdate
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.scalacheck._
import play.api.libs.json._
import scala.language.implicitConversions

class SendRandomClientUpdates extends Simulation {

  implicit def jsonValToString(jsonVal: JsValue): String = jsonVal.toString()

  val baseUrl: String = sys.props.getOrElse("urlPrefix", "http://localhost:9000/v1")
  val numClients: Int = sys.props.getOrElse("numClients", "1").toInt
  val sendInterval: Int = sys.props.getOrElse("sendInterval", "1").toInt
  val endPoint = baseUrl + "/send"

  val httpConf = http
    .baseURL(baseUrl) // Here is the root for all relative URLs
    .contentTypeHeader("application/json")

  val genClientUpdate = for {
    id <- Gen.choose[Int](0, 999999)
    lat <- Gen.choose[Double](-90.0, 90.0)
    lng <- Gen.choose[Double](-180.0, 180.0)
    ele <- Gen.choose[Double](-100.0, 200.0)
    heading <- Gen.choose[Double](0.0, 360.0)
    timestamp <- Gen.choose[Int](0, 9999999)
  } yield ClientUpdate(id, lat, lng, ele, heading, timestamp)

  val genClientUpdates = Gen.listOfN(1, genClientUpdate)

  val feeder = Iterator.continually(Map("clientUpdates" -> (Json.toJson(genClientUpdates.sample.map(
    clientUpdates => {
      clientUpdates.head :: clientUpdates.tail.map(
        cu => ClientUpdate(clientUpdates.head.id, cu.lat, cu.lng, cu.ele, cu.heading, cu.timestamp)
      )
    })
  ))))

  lazy val chain = feed(feeder)
    .exec(http("request")
      .post(endPoint).body(StringBody("${clientUpdates}")))


  val scn = scenario("Send location updates")
    .repeat(10) {
      chain.pause(sendInterval)
    }

  setUp(scn.inject(atOnceUsers(numClients)).protocols(httpConf))
}

