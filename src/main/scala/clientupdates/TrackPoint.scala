package clientupdates

import clientupdates.TrackPoint.AnnotationValue.AnnotationValue
import clientupdates.TrackPoint.{Accelerometer, Annotation, Compass, GpsMeta, Orientation}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

import scala.util.Try


case class TrackPoint(lat: Double,
                      lng: Double,
                      timestamp: Int,
                      ele: Option[Double] = None,
                      gpsMeta: Seq[GpsMeta] = Seq.empty,
                      compass: Seq[Compass] = Seq.empty,
                      accelerometer: Seq[Accelerometer] = Seq.empty,
                      orientation: Seq[Orientation] = Seq.empty,
                      annotation: Seq[Annotation] = Seq.empty
                     )

object TrackPoint {
  object AnnotationValue extends Enumeration {
    type AnnotationValue = Value
    val StartJibe = Value("START_JIBE")
    val MidJibe = Value("MID_JIBE")
    val EndJibe = Value("END_JIBE")

    val StartTacking = Value("START_TACKING")
    val MidTacking = Value("MID_TACKING")
    val EndTacking = Value("END_TACKING")

    val UnknownAnnotation = Value("UNKNOWN_ANNOTATION")

    implicit val annotationValueFormat = new Format[AnnotationValue] {
      def reads(json: JsValue): JsResult[Value] = {
        json
          .asOpt[String]
          .flatMap(str =>
            Try(JsSuccess(AnnotationValue.withName(str))).toOption)
          .getOrElse(JsSuccess(AnnotationValue.UnknownAnnotation))
      }

      def writes(enum: AnnotationValue): JsString = JsString(enum.toString)
    }
  }

  case class Compass(deg: Double, toffset: Int)
  case class Accelerometer(x: Double, y: Double, z: Double, toffset: Int)
  case class Orientation(azimuth: Double,
                         pitch: Double,
                         roll: Double,
                         toffset: Int)
  case class GpsMeta(accuracy: Double, satCount: Int, toffset: Int)
  case class Annotation(`type`: AnnotationValue, toffset: Int)


  implicit val compassFormat = Json.format[Compass]
  implicit val accelerometerFormat = Json.format[Accelerometer]
  implicit val orientationFormat = Json.format[Orientation]
  implicit val gpsMetaFormat = Json.format[GpsMeta]
  implicit val annotationFormat = Json.format[Annotation]

  def optionalSeq[T: Format](path: JsPath): OFormat[Seq[T]] =
    path
      .formatNullable[Seq[T]]
      .inmap(
        o => o.getOrElse(Seq.empty),
        s => Some(s)
      )

  implicit val trackPointFormat: Format[TrackPoint] = {
    (
      (JsPath \ "lat").format[Double](min(-90.0) keepAnd max(90.0)) and
        (JsPath \ "lng").format[Double](min(-180.0) keepAnd max(180.0)) and
        (JsPath \ "timestamp").format[Int](min(0)) and
        (JsPath \ "ele").formatNullable[Double] and
        optionalSeq[GpsMeta](JsPath \ "gpsMeta") and
        optionalSeq[Compass](JsPath \ "compass") and
        optionalSeq[Accelerometer](JsPath \ "accelerometer") and
        optionalSeq[Orientation](JsPath \ "orientation") and
        optionalSeq[Annotation](JsPath \ "annotation")
      )(TrackPoint.apply, unlift(TrackPoint.unapply))
  }
}