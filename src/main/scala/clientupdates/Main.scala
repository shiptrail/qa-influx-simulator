package clientupdates

import io.gatling.app.{Gatling, SelectedSimulationClass}
import org.rogach.scallop._
import simulations.{SendFileBasedClientUpdates, SendRandomClientUpdates}

import scala.concurrent.duration._

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val name = BuildInfo.name
  val buildVersion = BuildInfo.version
  val maintainer = BuildInfo.maintainer
  version(s"$name $buildVersion - (C) $maintainer")
  banner(
    """Usage: insi [OPTION]... [FILE]
      |
      |insi is designed to simulate gps data logger clients mounted
      |on boats. It does so by either generating pseudo random data
      |(for load testing) or replaying and multiplexing pre-recorded
      |gps trail data.
      |
      |If FILE is not specified random trail data is generated.
      |OPTIONS:
      | """.stripMargin)
  val server = opt[String](
    descr = "URL to backend-server (default: 'http://localhost:9000/v2')",
    default = Some("http://localhost:9000/v2")
  )
  val clients = opt[Int](
    descr = "the number of concurrent clients (aka. sailing boats) to be simulated (default: 50)",
    default = Some(50))
  val delay = opt[Int](
    descr = "time in ms to wait between sending a data batch (default: 10)",
    default = Some(10)
  ).map(_.milliseconds)
  val batchSize = opt[Int](
    descr = "number of location updates in one data batch (default: 10)",
    default = Some(10))
  val file: ScallopOption[String] = trailArg[String](
    descr = "path to a GPX, TCX or FIT file",
    required = false
  )
  verify()
}

object Main extends App {

  val conf: Conf = new Conf(args)

  val simulation = conf.file.toOption match {
    case Some(fileName) => fileBased()
    case None => randomBased()
  }
  Gatling.fromArgs(Array(), simulation)

  def fileBased() =
    Some(classOf[MainSendFileBasedClientUpdates]).asInstanceOf[SelectedSimulationClass]

  def randomBased() =
    Some(classOf[MainSendRandomClientUpdates]).asInstanceOf[SelectedSimulationClass]

  class MainSendFileBasedClientUpdates extends SendFileBasedClientUpdates(Main.conf)

  class MainSendRandomClientUpdates extends SendRandomClientUpdates(Main.conf)
}


