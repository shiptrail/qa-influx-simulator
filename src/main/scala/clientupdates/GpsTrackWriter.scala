package clientupdates

import play.api.libs.json.Json

object GpsTrackWriter {
  val supportedFormats = List("cgps", "gpx")
  def from(trackPoints: Iterator[TrackPoint]): GpsTrackWriter = new GpsTrackWriter(trackPoints)
}

class GpsTrackWriter(iter: Iterator[TrackPoint]) extends Iterator[TrackPoint] {
  val supportedFileFormats = Map[String, () => Iterator[String]](
    "cgps" -> toCustomFormat,
    "gpx" -> toGpxFormat
  )

  override def hasNext: Boolean = iter.hasNext

  override def next(): TrackPoint = iter.next()

  def to(fileFormat: String): Iterator[String] =
    supportedFileFormats.getOrElse(fileFormat, () => {
      System.err.println(s"Error: unsupported file format: ${fileFormat}")
      Iterator.empty
    })()


  def toCustomFormat(): Iterator[String] = iter.map(Json.toJson(_).toString())

  def toGpxFormat(): Iterator[String] = GpxFileBuilder.buildFrom(iter)
}
