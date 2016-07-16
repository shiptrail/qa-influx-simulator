package clientupdates

object MultiFormatParser {
  def parse(fileName: String): Iterator[TrackPoint] = {
    val gpxFileExtension = ".gpx"
    val tcxFileExtension = ".tcx"
    val fitFileExtension = ".fit"

    fileName match {
      case fileName if fileName.endsWith(gpxFileExtension) => GpxParser.parse(fileName)
      case fileName if fileName.endsWith(tcxFileExtension) => TcxParser.parse(fileName)
      case fileName if fileName.endsWith(fitFileExtension) => FitParser.parse(fileName)
      case _ => throw new Exception("Unsupported file type!")
    }
  }
}
