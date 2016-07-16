import clientupdates.BetterString
import org.scalatest.{FlatSpec, Matchers}

class BetterStringSpec extends FlatSpec with Matchers {
  "A BetterString" should "be able to return a replacement string if original String is empty" in {
    val emptyBetterString = new BetterString("")
    val nonEmptyBetterString = new BetterString("b")
    emptyBetterString.ifEmpty("") should be("")
    emptyBetterString.ifEmpty("a") should be("a")
    nonEmptyBetterString.ifEmpty("a") should be("b")
  }

  it should "be able to parse a GPS time stamp" in {
    val shortTimeString = "2013-07-20T16:24:57Z"
    val longTimeString = "2013-07-20T16:24:57.000Z"
    new BetterString(shortTimeString).toUnixTime() should be(1374330297)
    new BetterString(longTimeString).toUnixTime() should be(1374330297)
  }

  it should "return a negative int for invalid GPS timestamps" in {
    val emptyTimeString = ""
    val beforeUnixEpochTimeString = "1969-07-20T16:24:57.000Z"
    new BetterString(emptyTimeString).toUnixTime() should be < 0
    new BetterString(beforeUnixEpochTimeString).toUnixTime() should be < 0
  }
}
