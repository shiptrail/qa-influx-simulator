package simulations

import java.util.UUID

import clientupdates.{Conf, TrackPoint}
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import play.api.libs.json.Json

class SendClientUpdates(conf: Conf)(clientUpdates: Seq[TrackPoint])
    extends Simulation {
  require(conf != null)

  val endPoint = "send"

  val httpConf = http
    .baseURL(conf.server()) // Here is the root for all relative URLs
    .contentTypeHeader("application/json")

  def request: HttpRequestBuilder =
    http("request")
      .post("${endPointWithId}")
      .body(StringBody("${currentBatch}"))

  lazy val chain = exec(session => {
    session.set("currentBatch",
                Json.toJson(
                    session("updates")
                      .as[Iterator[TrackPoint]]
                      .take(conf.batchSize())
                      .toList))
  }).exec(request)

  val scn =
    scenario("Send location updates")
      .exec(session => {
        val id = UUID.randomUUID()
        val updates = clientUpdates.toIterator
        session
          .set("id", id)
          .set("endPointWithId", s"${conf.server()}/$id/$endPoint")
          .set("updates", updates)
      })
      .repeat(
          Math.ceil(clientUpdates.length.toDouble / conf.batchSize()).toInt) {
        chain.pause(conf.delay())
      }

  setUp(scn.inject(atOnceUsers(conf.clients())).protocols(httpConf))
}
