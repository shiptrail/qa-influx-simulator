package clientupdates

import io.gatling.app.Gatling
import io.gatling.core.scenario.Simulation
import org.rogach.scallop._
import simulations.{SendFileBasedClientUpdates, SendRandomClientUpdates}

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
  val server = opt[String](descr = "URL to backend-server (default: 'http://localhost:9000/v2')")
  val clients = opt[Int](descr = "the number of concurrent clients (aka. sailing boats) to be simulated (default: 50)")
  val delay = opt[Int](descr = "time in ms to wait between sending a data batch (default: 10)")
  val batchSize = opt[Int](descr = "number of location updates in one data batch (default: 10)")
  val file = trailArg[String](descr = "path to a GPX, TCX or FIT file", required = false)
  verify()
}

object Main {
  type SimulationFactory = (Class[_ <: Simulation]) => Simulation
  type SelectedSimulationClass = Option[Class[Simulation]]

  def main(args: Array[String]) {
    val conf: Conf = new Conf(args)

    getPropsForSetCmds(conf).foreach{
      case (key, value) => sys.props += (key -> value)
    }

    conf.file.toOption match {
      case Some(fileName) => fileBased()
      case None => randomBased()
    }
  }

  def fileBased(): Unit = {
    val classToLoad = Some(classOf[SendFileBasedClientUpdates]).asInstanceOf[SelectedSimulationClass]
    Gatling.fromArgs(Array(), classToLoad)
  }

  def randomBased(): Unit = {
    val classToLoad = Some(classOf[SendRandomClientUpdates]).asInstanceOf[SelectedSimulationClass]
    Gatling.fromArgs(Array(), classToLoad)
  }

  def getPropsForSetCmds(conf: Conf): Map[String, String] = {
    val parameterToPropMapping: Map[String, ScallopOption[_ >: Int with String]] = Map(
      "fileName" -> conf.file,
      "urlPrefix" -> conf.server,
      "numClients" -> conf.clients,
      "sendDelay" -> conf.delay,
      "batchSize" -> conf.batchSize
    )

    parameterToPropMapping.filter((t) => t._2
      .isDefined).map {
      case (key, value) => (key -> value.toOption.get.toString)
    }
  }
}


