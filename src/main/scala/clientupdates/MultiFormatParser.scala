package clientupdates

import scala.util.Try

object MultiFormatParser {
  val parserForExtension = Map(
    "gpx" -> GpxParser,
    "tcx" -> TcxParser,
    "fit" -> FitParser
  )

  def parse(fileName: String): Iterator[TrackPoint] = {
    parserForFileName(fileName) match {
      case Some(parserForFile) => parserForFile.parse(fileName)
      case None => {
        System.err.println("Unsupported file type!")
        Iterator.empty
      }
    }
  }

  def parserForFileName(fileName: String): Option[GpsTrackParser] = {
    val fileExtension = fileName.split('.').last
    parserForExtension.get(fileExtension)
  }
}
