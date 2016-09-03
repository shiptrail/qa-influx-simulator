package clientupdates

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ClientUpdate(
                         lat: Double,
                         lng: Double,
                         ele: Double,
                         heading: Double,
                         timestamp: Int,
                         accelerometer: Seq[Accelerometer]
                       )



object ClientUpdate {
  implicit val accelerometerFormat: Format[Accelerometer] = (
      (JsPath \ "x").format[Double] and
      (JsPath \ "y").format[Double] and
      (JsPath \ "z").format[Double] and
      (JsPath \ "toffset").format[Int]
    )(Accelerometer.apply, unlift(Accelerometer.unapply))

  implicit val clientUpdate: Format[ClientUpdate] = (
      (JsPath \ "lat").format[Double] and
      (JsPath \ "lng").format[Double] and
      (JsPath \ "ele").format[Double] and
      (JsPath \ "heading").format[Double] and
      (JsPath \ "timestamp").format[Int] and
      (JsPath \ "accelerometer").format[Seq[Accelerometer]]
    ) (ClientUpdate.apply, unlift(ClientUpdate.unapply))
}