package controllers

import javax.inject._

import play.api._
import play.api.libs.json.{JsError, Json}
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import sun.awt.X11.XTranslateCoordinates

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class BackendController @Inject() extends Controller {

  case class Coordinate(lat: Double, lng: Double, ele: Double, timestamp: Int)

  implicit val coordinateRead = Json.reads[Coordinate]

  def locationUpdates = Action(BodyParsers.parse.json) { request =>
    val coordinates = request.body.validate[Seq[Coordinate]]
    coordinates.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      coordinates => {
        val invalidCoordinates = coordinates.filter {
          coord => coord.timestamp <= 0 || coord.lat < -90 || coord.lat > 90 || coord.lng < -180 || coord.lat > 180
        }
        if (invalidCoordinates.length > 0) {
          println("[WARN] bad request in: " + coordinates)
          BadRequest(Json.obj("status" -> "KO", "message" -> "Invalid value range"))
        }
        else {
          println("got a request with: " + coordinates)
          Ok
        }
      }
    )
  }
}
