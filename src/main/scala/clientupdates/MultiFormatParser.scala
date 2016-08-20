package clientupdates

import java.io.File

object MultiFormatParser {
  val parserForExtension = Map(
    "gpx" -> GpxParser,
    "tcx" -> TcxParser,
    "fit" -> FitParser
  )

  def parse(file: File): Iterator[TrackPoint] = {
    parserForFileName(file) match {
      case Some(parserForFile) => parserForFile.parse(file)
      case None =>
        System.err.println("Error: Unsupported file type!")
        Iterator.empty
    }
  }

  def parserForFileName(file: File): Option[GpsTrackParser] = {
    val fileExtension = file.getName.split('.').last
    parserForExtension.get(fileExtension)
  }
}
