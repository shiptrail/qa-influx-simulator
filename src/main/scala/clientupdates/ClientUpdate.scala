package clientupdates

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ClientUpdate(id: Int, lat: Double, lng: Double, ele: Double, heading: Double, timestamp: Int)

object ClientUpdate {
  implicit val clientUpdate: Format[ClientUpdate] = (
    (JsPath \ "id").format[Int] and
      (JsPath \ "lat").format[Double] and
      (JsPath \ "lng").format[Double] and
      (JsPath \ "ele").format[Double] and
      (JsPath \ "heading").format[Double] and
      (JsPath \ "timestamp").format[Int]
    ) (ClientUpdate.apply, unlift(ClientUpdate.unapply))
}