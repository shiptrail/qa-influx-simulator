import clientupdates.BetterString
import org.scalatest.{FlatSpec, Matchers}

class BetterStringSpec extends FlatSpec with Matchers {
  "A BetterString" should "be able to parse a GPS time stamp" in {
    val shortTimeString = "2013-07-20T16:24:57Z"
    val longTimeString = "2013-07-20T16:24:57.000Z"
    new BetterString(shortTimeString).toUnixTime() should be(1374330297)
    new BetterString(longTimeString).toUnixTime() should be(1374330297)
  }
/*
  it should "return a negative int for invalid GPS timestamps" in {
    val beforeUnixEpochTimeString = "1969-07-20T16:24:57.000Z"
    new BetterString(beforeUnixEpochTimeString).toUnixTime() should be < 0
  }
  */
}
