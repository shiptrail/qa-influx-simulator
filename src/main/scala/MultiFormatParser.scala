package clientupdates

import java.io.InputStream

object MultiFormatParser extends GpsTrackParser{
  override def parse(fileName: String): Iterator[TrackPoint] = {
    val gpxFileExtension = ".gpx"
    val tcxFileExtension = ".tcx"

    fileName match {
      case fileName if fileName.endsWith(gpxFileExtension) => GpxParser.parse(fileName)
      case fileName if fileName.endsWith(tcxFileExtension) => TcxParser.parse(fileName)
      case _ => throw new Exception("Unsupported file type!")
    }
  }

  override def parse(inputStream: InputStream): Iterator[TrackPoint] = ???
}
