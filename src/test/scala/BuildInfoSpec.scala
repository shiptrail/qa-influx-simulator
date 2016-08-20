import clientupdates.BuildInfo
import org.scalatest._

class BuildInfoSpec extends FlatSpec with Matchers {
  "BuildInfo" should "should contain all relevant data from build.sbt" in {
    BuildInfo.name shouldBe a [String]
    BuildInfo.version shouldBe a [String]
    BuildInfo.scalaVersion shouldBe a [String]
    BuildInfo.sbtVersion shouldBe a [String]
    BuildInfo.maintainer shouldBe a [String]
  }
}