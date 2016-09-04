package clientupdates

import java.io.ByteArrayInputStream

import clientupdates.TrackPoint.Accelerometer
import org.scalatest._
import play.api.libs.json.Json

class GpxFileBuilderSpec extends FlatSpec with Matchers {
  val trackpoints = List(
    TrackPoint(52.4323181994, 13.1817903835, 1470655045, Some(45.85)),
    TrackPoint(52.4323181156, 13.1817902997, 1470655046, Some(45.85))
  )

  val trackpointsWithAccelerometer = List(
    TrackPoint(52.4323181994,13.1817903835,1470655045, Some(45.85), accelerometer = List(
      Accelerometer(-0.1,0.0,-1.0,17), Accelerometer(-0.1,0.0,-1.0,117), Accelerometer(-0.2,0.0,-1.0,217),
      Accelerometer(-0.3,-0.2,-1.1,317), Accelerometer(-0.2,-0.2,-1.0,417), Accelerometer(-0.1,-0.1,-1.1,517),
      Accelerometer(-0.1,-0.1,-1.1,617), Accelerometer(0.0,-0.1,-1.1,717), Accelerometer(0.0,-0.1,-1.1,817),
      Accelerometer(0.0,-0.1,-1.0,917))
    ),
    TrackPoint(52.4323181156,13.1817902997,1470655046, Some(45.85), accelerometer = List(
      Accelerometer(0.0,-0.1,-1.0,49), Accelerometer(0.0,-0.1,-1.0,149), Accelerometer(0.0,-0.1,-1.0,249),
      Accelerometer(0.0,-0.1,-1.0,349), Accelerometer(-0.1,-0.1,-1.0,449), Accelerometer(-0.1,-0.1,-1.0,553),
      Accelerometer(-0.1,-0.1,-1.0,652), Accelerometer(-0.1,-0.1,-1.0,752), Accelerometer(-0.1,-0.1,-1.0,852),
      Accelerometer(-0.1,-0.1,-1.0,952))
    )
  )

  val expectedGpxWithoutAccel = """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
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
                                  |    <time>2016-09-04T15:29:24Z</time>
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

  val expectedGpxWithAccel = """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
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
                               |    <time>2016-09-04T15:10:37Z</time>
                               |  </metadata>
                               |  <trk>
                               |    <name>created with insi</name>
                               |    <trkseg>
                               |      <trkpt lat="52.4323181994" lon="13.1817903835">
                               |        <ele>45.85</ele>
                               |        <time>2016-08-08T13:17:25Z</time>
                               |        <extensions>
                               |          <gpxacc:AccelerationExtension>
                               |            <gpxacc:accel offset="17" x="-0.1" y="0.0" z="-1.0"/>
                               |            <gpxacc:accel offset="117" x="-0.1" y="0.0" z="-1.0"/>
                               |            <gpxacc:accel offset="217" x="-0.2" y="0.0" z="-1.0"/>
                               |            <gpxacc:accel offset="317" x="-0.3" y="-0.2" z="-1.1"/>
                               |            <gpxacc:accel offset="417" x="-0.2" y="-0.2" z="-1.0"/>
                               |            <gpxacc:accel offset="517" x="-0.1" y="-0.1" z="-1.1"/>
                               |            <gpxacc:accel offset="617" x="-0.1" y="-0.1" z="-1.1"/>
                               |            <gpxacc:accel offset="717" x="0.0" y="-0.1" z="-1.1"/>
                               |            <gpxacc:accel offset="817" x="0.0" y="-0.1" z="-1.1"/>
                               |            <gpxacc:accel offset="917" x="0.0" y="-0.1" z="-1.0"/>
                               |          </gpxacc:AccelerationExtension>
                               |        </extensions>
                               |      </trkpt>
                               |      <trkpt lat="52.4323181156" lon="13.1817902997">
                               |        <ele>45.85</ele>
                               |        <time>2016-08-08T13:17:26Z</time>
                               |        <extensions>
                               |          <gpxacc:AccelerationExtension>
                               |            <gpxacc:accel offset="49" x="0.0" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="149" x="0.0" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="249" x="0.0" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="349" x="0.0" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="449" x="-0.1" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="553" x="-0.1" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="652" x="-0.1" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="752" x="-0.1" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="852" x="-0.1" y="-0.1" z="-1.0"/>
                               |            <gpxacc:accel offset="952" x="-0.1" y="-0.1" z="-1.0"/>
                               |          </gpxacc:AccelerationExtension>
                               |        </extensions>
                               |      </trkpt>
                               |    </trkseg>
                               |  </trk>
                               |</gpx>
                               |""".stripMargin

  it should "be able to create a valid gpx file from trackpoints" in {
    val createdGpxFile = GpxFileBuilder.buildFrom(trackpoints.toIterator)
    val createdGpxFileXml = xml.XML.loadString(createdGpxFile.mkString("\n"))
    val expectedGpxWithoutAccelXml = xml.XML.loadString(expectedGpxWithoutAccel)

    createdGpxFileXml \ "trk" \ "trkseg" \ "trkpt"  should be(expectedGpxWithoutAccelXml \ "trk" \ "trkseg" \ "trkpt")
  }

  it should "be able to create a valid gpx file from trackpoints with accelerometer data" in {
    val createdGpxFile = GpxFileBuilder.buildFrom(trackpointsWithAccelerometer.toIterator)
    val createdGpxFileXml = xml.XML.loadString(createdGpxFile.mkString("\n"))
    val expectedGpxWithAccelXml = xml.XML.loadString(expectedGpxWithAccel)

    createdGpxFileXml \ "trk" \ "trkseg" \ "trkpt"  should be(expectedGpxWithAccelXml \ "trk" \ "trkseg" \ "trkpt")
  }
}
