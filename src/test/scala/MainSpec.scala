import clientupdates._
import org.scalatest.{FlatSpec, Matchers}

class MainSpec extends FlatSpec with Matchers {
  "getPropsForSetCmds" should "should contain all relevant data from build.sbt" in {
    val someOptionsSet = new Conf(Array("-b10", "-d99"))
    val propsToSet = Main.getPropsForSetCmds(someOptionsSet)

    propsToSet.size should be(2)
    propsToSet("batchSize") should be("10")
    propsToSet("sendDelay") should be("99")
  }
}