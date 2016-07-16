package clientupdates

object MultiFormatParser {
  val parserForExtension = Map(
    "gpx" -> GpxParser,
    "tcx" -> TcxParser,
    "fit" -> FitParser
  )

  def parse(fileName: String): Iterator[TrackPoint] = {
    val parser = parserForFileName(fileName)
    parser.parse(fileName)
  }

  def parserForFileName(fileName: String): GpsTrackParser = {
    val fileExtension = fileName.split('.').last
    parserForExtension.getOrElse(fileExtension, throw new Exception("Unsupported file type!"))
  }
}
