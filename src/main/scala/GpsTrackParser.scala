package clientupdates

import java.io.{FileInputStream, InputStream}

class GpsTrackParser {
  def parse(fileName: String): Iterator[TrackPoint] = {
    val fileInputStream = new FileInputStream(fileName)
    parse(fileInputStream)
  }

  def parse(inputStream: InputStream): Iterator[TrackPoint] = ???
}
