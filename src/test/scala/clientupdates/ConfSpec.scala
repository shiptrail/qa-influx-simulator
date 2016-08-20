package clientupdates

import org.scalatest.{FlatSpec, Matchers}

class ConfSpec extends FlatSpec with Matchers {

  "ConfSpec" should "should work with empty args" in {
    val confWithEmptyArgs = new Conf(Array[String]())
    import confWithEmptyArgs._

    file should not be 'defined
    all(List(batchSize, clients, delay, server)) should be('defined)
  }

  "ConfSpec" should "should work with some args set" in {
    val confWithSomeArgs = new Conf(Array[String]("-c1", "../lala"))
    import confWithSomeArgs._

    file should be('defined)
    all(List(batchSize, delay, server, clients)) should be ('defined)

    clients() should be(1)
    file() should be("../lala")
  }
}