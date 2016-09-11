package clientupdates

import play.api.libs.json.Json

object GpsEventWriter {
  def from(trackPoints: Iterator[TrackPoint]): GpsEventWriter = new GpsEventWriter(trackPoints)
}

class GpsEventWriter(iter: Iterator[TrackPoint]) extends Iterator[TrackPoint] {
  override def hasNext: Boolean = iter.hasNext

  override def next(): TrackPoint = iter.next()

  def toTrackPointStrings: Iterator[String] = iter.filter(_.annotation.length > 0).map(Json.toJson(_)
    .toString())
}
