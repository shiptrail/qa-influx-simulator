package clientupdates

import clientupdates.TrackPoint.Accelerometer
import org.joda.time.format.{DateTimeFormat, ISODateTimeFormat}
import org.joda.time.{DateTime, DateTimeZone}

/**
  * Created by phiros on 04.09.16.
  */
object GpxFileBuilder {
  def buildFrom(iter: Iterator[TrackPoint]): Iterator[String] = {
    val createTime = new DateTime()
      .withZone(DateTimeZone.UTC)
      .toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z"))
    val trackName = "created with insi"
    val prologue =
      s"""<?xml version="1.0" encoding="UTF-8" standalone="no"?>
          |<gpx xmlns="http://www.topografix.com/GPX/1/1"
          |     xmlns:gpxx="http://www.garmin.com/xmlschemas/GpxExtensions/v3"
          |     xmlns:gpxtrkx="http://www.garmin.com/xmlschemas/TrackStatsExtension/v1"
          |     xmlns:gpxtrkoffx="http://www.garmin.com/xmlschemas/TrackMovieOffsetExtension/v1"
          |     xmlns:wptx1="http://www.garmin.com/xmlschemas/WaypointExtension/v1"
          |     xmlns:gpxtpx="http://www.garmin.com/xmlschemas/TrackPointExtension/v1"
          |     xmlns:gpxpx="http://www.garmin.com/xmlschemas/PowerExtension/v1"
          |     xmlns:gpxacc="http://www.garmin.com/xmlschemas/AccelerationExtension/v1"
          |     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          |     creator="insi"
          |     version="1.1">
          |  <metadata>
          |    <link href="http://www.garmin.com">
          |      <text>Garmin International</text>
          |    </link>
          |    <time>${createTime}</time>
          |  </metadata>
          |  <trk>
          |    <name>${trackName}</name>
          |    <trkseg>
          |""".stripMargin

    val epilogue =
      """    </trkseg>
        |  </trk>
        |</gpx>""".stripMargin



    val trackPointsAsGpx = iter.map(trackpointToGpx)
    prologue.split("\n").toIterator ++ trackPointsAsGpx ++ epilogue.split("\n").toIterator
  }

  def trackpointToGpx(tp: TrackPoint): String = {
    val timeInIso = new DateTime(tp.timestamp * 1000L)
      .withZone(DateTimeZone.UTC)
      .toString(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z"))
    val extensions = buildExtensions(tp)

    val prologue =
      s"""      <trkpt lat="${tp.lat}" lon="${tp.lng}">
          |        <ele>${tp.ele.getOrElse(0.0)}</ele>
          |        <time>${timeInIso}</time>""".stripMargin

    val epilogue = if (extensions.nonEmpty) {
      s"""        ${extensions}
          |      </trkpt>""".stripMargin
    } else {
      s"""      </trkpt>"""
    }

    s"""${prologue}
       |${epilogue}""".stripMargin
  }

  def buildExtensions(tp: TrackPoint): String = {
    val prologue = """<extensions>"""
    val epilogue = """        </extensions>"""

    val potentialExtensionFields = List(
      buildAccelerationExtension(tp)
    )
    val definedExtensions = potentialExtensionFields.filter(_.isDefined).map(_.get)

    if (definedExtensions.nonEmpty) {
      s"""${prologue}
         |${definedExtensions.mkString("\n")}
         |${epilogue}""".stripMargin
    } else {
      ""
    }
  }

  def buildAccelerationExtension(tp: TrackPoint): Option[String] = {
    val prologue = """          <gpxacc:AccelerationExtension>"""
    val epilogue = """          </gpxacc:AccelerationExtension>"""
    def accelerometerToGpx(a: Accelerometer) =
      s"""            <gpxacc:accel offset="${a.toffset}" x="${a.x}" y="${a.y}" z="${a.z}"/>"""

    if (tp.accelerometer.isEmpty) None
    else {
      val dataPoints = tp.accelerometer.map(accelerometerToGpx).mkString("\n")
      Some(
        s"""${prologue}
           |${dataPoints}
           |${epilogue}""".stripMargin
      )
    }
  }
}
