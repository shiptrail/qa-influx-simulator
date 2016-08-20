package clientupdates

import java.io.{ByteArrayInputStream, FileNotFoundException, IOException, InputStream}
import java.net.URL

import scala.sys.process._
import scala.util.Try

object FitParser extends GpsTrackParser {

  override def parse(fileName: String): Iterator[TrackPoint] = {
    parse(new java.io.File(fileName).toURI.toURL)
  }

  def parse(url: URL): Iterator[TrackPoint] = {
    val stdErrSuppressor = ProcessLogger(line => {})

    val gpsBabelVersion = Try("gpsbabel -V".!!(stdErrSuppressor))
    if (gpsBabelVersion.isFailure) {
      System.err.println("Error: Couldn't find gpsbabel in your path!")
      return Iterator.empty
    }

    val statUrl = Try(scala.io.Source.fromURL(url).take(1))
    if (statUrl.isFailure) {
      System.err.println(s"Error: Couldn't read file: $url")
      return Iterator.empty
    }

    val gpsBabelOutput = Try((url #> "gpsbabel -i garmin_fit -f - -o gpx -F -").!!(stdErrSuppressor).trim())
    gpsBabelOutput.flatMap(output => Try(parse(new ByteArrayInputStream(output.getBytes)))).getOrElse({
      System.err.println(s"Error: Something went wrong whilst calling gpsbabel!")
      Iterator.empty
    })
  }

  override def parse(is: InputStream): Iterator[TrackPoint] = {
    GpxParser.parse(is)
  }
}
