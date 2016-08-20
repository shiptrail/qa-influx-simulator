package clientupdates

import org.scalatest.{FlatSpec, Matchers}

class MultiFormatParserSpec extends FlatSpec with Matchers {
  "parserForFileName" should "be able to determine the correct parser type" in {
    val gpxFileName = "foobar.gpx"
    val tcxFileName = "foobar.tcx"
    val fitFileName = "foobar.fit"

    MultiFormatParser.parserForFileName(gpxFileName) should be(Some(GpxParser))
    MultiFormatParser.parserForFileName(tcxFileName) should be(Some(TcxParser))
    MultiFormatParser.parserForFileName(fitFileName) should be(Some(FitParser))
  }

  "parserForFileName" should "return None for an unknown file extension" in {
    val unknownFileExtension = "foobar.foobar"

    MultiFormatParser.parserForFileName(unknownFileExtension) should be(None)
  }

  "parse" should "return an empty iterator for an unknown file extension" in {
    val unknownFileExtension = "foobar.foobar"

    MultiFormatParser.parse(unknownFileExtension) should be(Iterator.empty)
  }
}
