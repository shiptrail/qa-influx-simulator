import clientupdates.{FitParser, GpxParser, MultiFormatParser, TcxParser}
import org.scalatest.{FlatSpec, Matchers}

import scala.language.reflectiveCalls

class MultiFormatParserSpec extends FlatSpec with Matchers {
  "MultiFormatParser" should "be able to determine the correct parser type" in {
    val gpxFileName = "foobar.gpx"
    val tcxFileName = "foobar.tcx"
    val fitFileName = "foobar.fit"

    MultiFormatParser.parserForFileName(gpxFileName) should be(GpxParser)
    MultiFormatParser.parserForFileName(tcxFileName) should be(TcxParser)
    MultiFormatParser.parserForFileName(fitFileName) should be(FitParser)
  }

  "MultiFormatParser" should "throw an exception for an unknown file type" in {
    val unknownFileExtension = "foobar.foobar"

    intercept[Exception] {
      MultiFormatParser.parserForExtension(unknownFileExtension)
    }
  }
}
