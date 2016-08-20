package clientupdates

import java.io.File

import io.gatling.app.{Gatling, SelectedSimulationClass}
import org.rogach.scallop._
import simulations.{SendFileBasedClientUpdates, SendRandomClientUpdates}

import scala.concurrent.duration._

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {
  val name = BuildInfo.name
  val buildVersion = BuildInfo.version
  val maintainer = BuildInfo.maintainer
  version(s"$name $buildVersion - (C) $maintainer")
  banner("""Usage: insi [OPTION]... [FILE]
      |
      |insi is designed to simulate gps data logger clients mounted on boats. It does
      |so by either generating pseudo random data (for load testing) or replaying and
      |multiplexing pre-recorded gps trail data.
      |
      |If FILE is not specified random trail data is generated.
      |
      |OPTIONS:
      | """.stripMargin)

  footer(
    """
      |Random Trail data
      |   The random generated adhere to the validation rules of the backend server.
      |   All clients using a fresh generated ID. The number of data points in one
      |   batch (read array) is hard-coded to 10.
      |
      |Replay File
      |   The simulator reads a file and replays it with a fresh ID. Supported file
      |   types are GPX, TCX and FIT.
      |
      |Benchmark output
      |   Gatling prints some information about te progress of the simulation and
      |   stores an HTML on the disk. (Path is printed after the simulation)
    """.stripMargin)
  val server = optWithDefault[String](
    descr = "URL to backend-server",
    default = "http://localhost:9000/v2"
  )
  val clients = optWithDefault[Int](
    descr =
      "the number of concurrent clients (aka. sailing boats) to be simulated",
    default = 50)
  val delay = optWithDefault[Int](
    descr = "time in ms to wait between sending a data batch",
    default = 10
  ).map(_.milliseconds)
  val batchSize = optWithDefault[Int](
    descr = "number of location updates in one data batch",
    default = 10)
  val file: ScallopOption[File] = trailArg[File](
    descr = "path to a GPX, TCX or FIT file",
    required = false
  )

  validate(file) {
    case ok if ok.isFile => Right(Unit)
    case notExisting if !notExisting.exists() =>
      Left("Could not find " + notExisting.getPath)
    case notAFile if !notAFile.isFile =>
      Left(notAFile.getPath + " is not a file")
  }

  verify()

  def optWithDefault[A: ValueConverter](descr: String,
                                        default: A): ScallopOption[A] =
    opt(descr = descr + s" (default: $default)", default = Some(default))
}

object Main extends App {

  val conf: Conf = new Conf(args)

  val simulation = conf.file.toOption match {
    case Some(fileName) => fileBased()
    case None => randomBased()
  }
  Gatling.fromArgs(Array(), simulation)

  def fileBased() =
    Some(classOf[MainSendFileBasedClientUpdates])
      .asInstanceOf[SelectedSimulationClass]

  def randomBased() =
    Some(classOf[MainSendRandomClientUpdates])
      .asInstanceOf[SelectedSimulationClass]

  class MainSendFileBasedClientUpdates
      extends SendFileBasedClientUpdates(Main.conf)

  class MainSendRandomClientUpdates extends SendRandomClientUpdates(Main.conf)
}
