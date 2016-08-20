package clientupdates

import java.io.File

import org.scalatest.{FlatSpec, Matchers}

class MultiFormatParserSpec extends FlatSpec with Matchers {
  "parserForFileName" should "be able to determine the correct parser type" in {
    val gpxFile = new File("foobar.gpx")
    val tcxFile = new File("foobar.tcx")
    val fitFile = new File("foobar.fit")

    MultiFormatParser.parserForFileName(gpxFile) should be(Some(GpxParser))
    MultiFormatParser.parserForFileName(tcxFile) should be(Some(TcxParser))
    MultiFormatParser.parserForFileName(fitFile) should be(Some(FitParser))
  }

  "parserForFileName" should "return None for an unknown file extension" in {
    val unknownFileExtension = new File("foobar.foobar")

    MultiFormatParser.parserForFileName(unknownFileExtension) should be(None)
  }

  "parse" should "return an empty iterator for an unknown file extension" in {
    val unknownFileExtension = new File("foobar.foobar")

    MultiFormatParser.parse(unknownFileExtension) should be(empty)
  }
}
