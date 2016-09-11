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
  val supportedExportFormats = GpsTrackWriter.supportedFormats.mkString(" ")
  version(s"$name $buildVersion - (C) $maintainer")
  banner(
    """Usage: insi [OPTION]... [FILE]
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
      |   types are GPX, TCX, FIT and CGPS.
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
  val dumpFileFormat = opt[String](
    descr = "instead of sending data to the backend write trail data to stdout in a file format of your choice. " +
      s"(supported: ${supportedExportFormats}) Note: all other options except file are ignored")
  val extractEvents = opt[Boolean](
    descr = "instead of sending data to the backend write event data to stdout " +
      "Note: all other options except file are ignored")
  val file: ScallopOption[File] = trailArg[File](
    descr = "path to a GPX, TCX, FIT or CGPS file",
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
  val fileName: Option[File] = conf.file.toOption
  val dumpFileFormat = conf.dumpFileFormat.toOption
  val extractEvents = conf.extractEvents.toOption.getOrElse(false)

  if (dumpFileFormat.isDefined) {
    val format = dumpFileFormat.get
    val source = getFileNameAsSource(fileName)
    GpsTrackWriter.from(source).to(format) foreach println
  } else if (extractEvents) {
    val source = getFileNameAsSource(fileName)
    GpsEventWriter.from(source).toTrackPointStrings foreach println
  }
  else {
    val simulation = fileName match {
      case Some(fileName) => fileBased()
      case None => randomBased()
    }
    Gatling.fromArgs(Array(), simulation)
  }

  def fileBased() =
    Some(classOf[MainSendFileBasedClientUpdates])
      .asInstanceOf[SelectedSimulationClass]

  def randomBased() =
    Some(classOf[MainSendRandomClientUpdates])
      .asInstanceOf[SelectedSimulationClass]

  def getFileNameAsSource(fileName: Option[File]): Iterator[TrackPoint] = {
    fileName match {
      case Some(fileName) => {
        MultiFormatParser.parse(fileName)
      }
      case None => {
        System.err.println("Error: dumping random data is not supported!")
        Iterator.empty
      }
    }
  }

  class MainSendFileBasedClientUpdates
    extends SendFileBasedClientUpdates(Main.conf)

  class MainSendRandomClientUpdates extends SendRandomClientUpdates(Main.conf)

}
