import clientupdates.StringUtils._
import org.scalatest.{FlatSpec, Matchers}

class StringUtilsSpec extends FlatSpec with Matchers {
  "toUnixTime" should "be able to parse a GPS time stamp" in {
    val shortTimeString = "2013-07-20T16:24:57Z"
    val longTimeString = "2013-07-20T16:24:57.000Z"
    toUnixTime(shortTimeString) should be(Some(1374330297))
    toUnixTime(longTimeString) should be(Some(1374330297))
  }

  it should "return None for unparseable time stamps" in {
    val invalidTimeStringCut = "2013-07-20T15"
    val invalidTimeStringEmpty = ""
    toUnixTime(invalidTimeStringCut) should be(None)
    toUnixTime(invalidTimeStringEmpty) should be(None)
  }

  "toDouble" should "be able to parse doubles" in {
    val fancyDouble = "42.42"
    val zero = "0.0"
    toDouble(fancyDouble) should be(Some(42.42))
    toDouble(zero) should be(Some(0.0))
  }

  it should "fail on invalid doubles" in {
    val weirdDouble = "1.z"
    val emptyString = ""
    toDouble(weirdDouble) should be(None)
    toDouble(emptyString) should be(None)
  }
}
