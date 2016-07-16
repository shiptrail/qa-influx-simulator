import clientupdates._
import org.scalatest.{FlatSpec, Matchers}

import scala.language.reflectiveCalls

class XmlBasedParserSpec extends FlatSpec with Matchers with XmlBasedParser {
  "whenDefined" should "return Some(string) if string is a non-empty text element in a NodeSeq" in {
    val expectedContent = "13.04"
    val nonEmptyXmlText = xml.XML.loadString(s"<xml><lat>$expectedContent</lat></xml>").child

    whenDefined(nonEmptyXmlText) should be(Some(expectedContent))
  }

  "whenDefined" should "return None if string is an empty text element in a NodeSeq" in {
    val expectedContent = ""
    val emptyXmlText = xml.XML.loadString(s"<xml><lat>$expectedContent</lat></xml>").child

    whenDefined(emptyXmlText) should be(None)
  }
}