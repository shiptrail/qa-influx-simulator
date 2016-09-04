package clientupdates

import org.scalatest._
import play.api.libs.json.Json

class GpsTrackWriterSpec extends FlatSpec with Matchers {
  val trackpoints = List(
    TrackPoint(52.4323181994, 13.1817903835, 1470655045, Some(45.85)),
    TrackPoint(52.4323181156, 13.1817902997, 1470655046, Some(45.85))
  )

  val expectedGpx = """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
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
                      |    <time>2016-09-04T15:39:09Z</time>
                      |  </metadata>
                      |  <trk>
                      |    <name>created with insi</name>
                      |    <trkseg>
                      |      <trkpt lat="52.4323181994" lon="13.1817903835">
                      |        <ele>45.85</ele>
                      |        <time>2016-08-08T13:17:25Z</time>
                      |      </trkpt>
                      |      <trkpt lat="52.4323181156" lon="13.1817902997">
                      |        <ele>45.85</ele>
                      |        <time>2016-08-08T13:17:26Z</time>
                      |      </trkpt>
                      |    </trkseg>
                      |  </trk>
                      |</gpx>
                      |""".stripMargin

  val expectedCustomFileFormat: Iterator[String] = trackpoints.map((tp) => Json.toJson(tp).toString()).toIterator

  it should "be able to generate an instance from trackpoints" in {
    val trackpointsIter = trackpoints.toIterator
    val writerTrackpoints = GpsTrackWriter.from(trackpointsIter).toList

    writerTrackpoints should be(trackpoints)
  }

  it should "be able to transform the trackpoint to cgps" in {
    val inputTrackpoints = trackpoints.toIterator
    val writerStringOutput = GpsTrackWriter.from(inputTrackpoints).to("cgps")

    writerStringOutput.mkString("\n") should be(expectedCustomFileFormat.mkString("\n"))
  }

  it should "be able to transform the trackpoint to gpx" in {
    val inputTrackpoints = trackpoints.toIterator
    val writerStringOutput = GpsTrackWriter.from(inputTrackpoints).to("gpx")
    val createdGpxFileXml = xml.XML.loadString(writerStringOutput.mkString("\n"))
    val expectedGpxXml = xml.XML.loadString(expectedGpx)

    createdGpxFileXml \ "trk" \ "trkseg" \ "trkpt"  should be(expectedGpxXml \ "trk" \ "trkseg" \ "trkpt")
  }
}