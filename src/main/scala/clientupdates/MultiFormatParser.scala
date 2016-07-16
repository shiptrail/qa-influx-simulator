package clientupdates

object MultiFormatParser {
  def parse(fileName: String): Iterator[TrackPoint] = {
    val parserForExtension = Map(
      "gpx" -> GpxParser,
      "tcx" -> TcxParser,
      "fit" -> FitParser
    )

    val fileExtension = fileName.split(".").last
    val parser = parserForExtension.getOrElse(fileExtension, throw new Exception("Unsupported file type!"))
    parser.parse(fileName)
  }
}
