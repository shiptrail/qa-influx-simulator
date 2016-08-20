package clientupdates

import java.io.{File, FileInputStream, InputStream}

trait GpsTrackParser {
  def parse(fileName: File): Iterator[TrackPoint] =
    parse(new FileInputStream(fileName))

  def parse(inputStream: InputStream): Iterator[TrackPoint]
}
