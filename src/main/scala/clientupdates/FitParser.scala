package clientupdates

import java.io.{ByteArrayInputStream, InputStream}
import java.net.URL

import scala.sys.process._

object FitParser extends GpsTrackParser {
  override def parse(fileName: String): Iterator[TrackPoint] = {
    parse(new java.io.File(fileName).toURI.toURL)
  }

  def parse(url: URL): Iterator[TrackPoint] = {
    try {
      val gpsBabelOutPut: String = (url #> "gpsbabel -i garmin_fit -f - -o gpx -F -").!!.trim()
      parse(new ByteArrayInputStream(gpsBabelOutPut.getBytes()))
    } catch {
      case _: RuntimeException => {
        stderr.println("WARNING: Couldn't find gpsbabel in your path!")
        Iterator.empty
      }
      case _: Throwable => {
        stderr.println("WARNING: Unknown error whilst calling gpsbabel in your path!")
        Iterator.empty
      }
    }
  }

  override def parse(is: InputStream): Iterator[TrackPoint] = {
    GpxParser.parse(is)
  }
}
