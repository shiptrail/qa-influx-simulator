import org.scalatest.{FlatSpec, Matchers}
import clientupdates.Conf

class ConfSpec extends FlatSpec with Matchers {

  "ConfSpec" should "should work with empty args" in {
    val confWithEmptyArgs = new Conf(Array[String]())

    confWithEmptyArgs.file.isDefined should be(false)
    confWithEmptyArgs.batchSize.isDefined should be(false)
    confWithEmptyArgs.clients.isDefined should be(false)
    confWithEmptyArgs.delay.isDefined should be(false)
    confWithEmptyArgs.server.isDefined should be(false)
  }

  "ConfSpec" should "should work with some args set" in {
    val confWithEmptyArgs = new Conf(Array[String]("-c1","../lala"))

    confWithEmptyArgs.file.isDefined should be(true)
    confWithEmptyArgs.batchSize.isDefined should be(false)
    confWithEmptyArgs.clients.isDefined should be(true)
    confWithEmptyArgs.delay.isDefined should be(false)
    confWithEmptyArgs.server.isDefined should be(false)
  }
}