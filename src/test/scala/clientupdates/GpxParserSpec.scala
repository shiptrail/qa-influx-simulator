package clientupdates

import java.io.ByteArrayInputStream

import org.scalatest._

class GpxParserSpec extends FlatSpec with Matchers {

  val minimalGpxFile =
    """
      <gpx version="1.1" creator="Garmin Connect"
       xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd"
       xmlns="http://www.topografix.com/GPX/1/1"
       xmlns:gpxtpx="http://www.garmin.com/xmlschemas/TrackPointExtension/v1"
       xmlns:gpxx="http://www.garmin.com/xmlschemas/GpxExtensions/v3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
       <metadata>
         <link href="connect.garmin.com">
           <text>Garmin Connect</text>
         </link>
         <time>2013-09-02T13:56:40.000Z</time>
       </metadata>
       <trk>
         <name>Segeln F-Kurs 420er</name>
         <desc>6/8 Bft :)</desc>
         <trkseg>
           <trkpt lon="13.209660965949297" lat="52.508182134479284">
             <ele>20.399999618530273</ele>
             <time>2013-09-02T13:56:40.000Z</time>
           </trkpt>
           <trkpt lon="13.209655852988362" lat="52.508178278803825">
             <ele>24.0</ele>
             <time>2013-09-02T13:56:41.000Z</time>
           </trkpt>
           <trkpt lon="13.209633054211736" lat="52.50816285610199">
             <ele>24.0</ele>
             <time>2013-09-02T13:56:47.000Z</time>
           </trkpt>
         </trkseg>
       </trk>
      </gpx>
    """

  val swimmingGpxFile =
    """
      |<gpx
      |  version="1.1"
      |  creator="RunKeeper - http://www.runkeeper.com"
      |  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      |  xmlns="http://www.topografix.com/GPX/1/1"
      |  xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd"
      |  xmlns:gpxtpx="http://www.garmin.com/xmlschemas/TrackPointExtension/v1">
      |<trk>
      |  <name><![CDATA[Swimming 7/20/13 3:37 pm]]></name>
      |  <time>2013-07-20T15:37:31Z</time>
      |<trkseg>
      |<trkpt lat="52.509451000" lon="13.209473000"><ele>36.0</ele><time>2013-07-20T15:37:31Z</time></trkpt>
      |<trkpt lat="52.509423000" lon="13.209413000"><ele>36.0</ele><time>2013-07-20T15:38:03Z</time></trkpt>
      |<trkpt lat="52.509328000" lon="13.209443000"><ele>35.4</ele><time>2013-07-20T15:59:09Z</time></trkpt>
      |<trkpt lat="52.509415000" lon="13.209395000"><ele>34.9</ele><time>2013-07-20T15:59:46Z</time></trkpt>
      |</trkseg>
      |</trk>
      |</gpx>
    """.stripMargin

  val gpxFileWithAcceleration =
    """
      |<gpx xmlns="http://www.topografix.com/GPX/1/1" xmlns:gpxx="http://www.garmin.com/xmlschemas/GpxExtensions/v3" xmlns:gpxtrkx="http://www.garmin.com/xmlschemas/TrackStatsExtension/v1" xmlns:gpxtrkoffx="http://www.garmin.com/xmlschemas/TrackMovieOffsetExtension/v1" xmlns:wptx1="http://www.garmin.com/xmlschemas/WaypointExtension/v1" xmlns:gpxtpx="http://www.garmin.com/xmlschemas/TrackPointExtension/v1" xmlns:gpxpx="http://www.garmin.com/xmlschemas/PowerExtension/v1" xmlns:gpxacc="http://www.garmin.com/xmlschemas/AccelerationExtension/v1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" creator="VIRB Elite" version="1.1">
      |  <metadata>
      |    <link href="http://www.garmin.com">
      |      <text>Garmin International</text>
      |    </link>
      |    <time>2016-08-08T14:48:10Z</time>
      |  </metadata>
      |  <trk>
      |    <name>2016-08-08 15:17:24</name>
      |    <link href="\DCIM\103_VIRB\VIRB0097.MP4"/>
      |    <extensions>
      |      <gpxx:TrackExtension>
      |        <gpxx:DisplayColor>Cyan</gpxx:DisplayColor>
      |      </gpxx:TrackExtension>
      |      <gpxtrkoffx:TrackMovieOffsetExtension>
      |        <gpxtrkoffx:StartOffsetSecs>0.889</gpxtrkoffx:StartOffsetSecs>
      |      </gpxtrkoffx:TrackMovieOffsetExtension>
      |    </extensions>
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
      |      <trkpt lat="52.4323181156" lon="13.1817902159">
      |        <ele>45.85</ele>
      |        <time>2016-08-08T13:17:27Z</time>
      |        <extensions>
      |          <gpxacc:AccelerationExtension>
      |            <gpxacc:accel offset="24" x="-0.1" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="124" x="-0.1" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="224" x="-0.1" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="324" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="424" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="524" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="624" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="724" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="824" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="924" x="-0.1" y="-0.1" z="-1.0"/>
      |          </gpxacc:AccelerationExtension>
      |        </extensions>
      |      </trkpt>
      |      <trkpt lat="52.4323180318" lon="13.1817901321">
      |        <ele>45.85</ele>
      |        <time>2016-08-08T13:17:28Z</time>
      |        <extensions>
      |          <gpxacc:AccelerationExtension>
      |            <gpxacc:accel offset="55" x="-0.1" y="0.0" z="-1.0"/>
      |            <gpxacc:accel offset="155" x="-0.1" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="255" x="-0.1" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="355" x="-0.1" y="0.0" z="-1.1"/>
      |            <gpxacc:accel offset="455" x="0.0" y="0.0" z="-1.1"/>
      |            <gpxacc:accel offset="555" x="0.0" y="-0.1" z="-1.1"/>
      |            <gpxacc:accel offset="655" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="755" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="855" x="0.0" y="-0.1" z="-1.0"/>
      |            <gpxacc:accel offset="955" x="0.0" y="-0.1" z="-1.0"/>
      |          </gpxacc:AccelerationExtension>
      |        </extensions>
      |      </trkpt>
      |    </trkseg>
      |  </trk>
      |</gpx>
    """.stripMargin

  val expectedResults = List(
    TrackPoint(52.508182134479284, 13.209660965949297, 20.399999618530273, 1378123000, Seq.empty),
    TrackPoint(52.508178278803825, 13.209655852988362, 24.0, 1378123001, Seq.empty),
    TrackPoint(52.50816285610199, 13.209633054211736, 24.0, 1378123007, Seq.empty)
  )

  val expectedResultsSwimmingFile = List(
    TrackPoint(52.509451,13.209473,36.0,1374327451, Seq.empty),
    TrackPoint(52.509423,13.209413,36.0,1374327483, Seq.empty),
    TrackPoint(52.509328,13.209443,35.4,1374328749, Seq.empty),
    TrackPoint(52.509415,13.209395,34.9,1374328786, Seq.empty)
  )

  val expectedResultsGpxWithAccelerationData = List(
    TrackPoint(52.4323181994,13.1817903835,45.85,1470655045,List(
      Accelerometer(-0.1,0.0,-1.0,17), Accelerometer(-0.1,0.0,-1.0,117), Accelerometer(-0.2,0.0,-1.0,217),
      Accelerometer(-0.3,-0.2,-1.1,317), Accelerometer(-0.2,-0.2,-1.0,417), Accelerometer(-0.1,-0.1,-1.1,517),
      Accelerometer(-0.1,-0.1,-1.1,617), Accelerometer(0.0,-0.1,-1.1,717), Accelerometer(0.0,-0.1,-1.1,817),
      Accelerometer(0.0,-0.1,-1.0,917))
    ),
    TrackPoint(52.4323181156,13.1817902997,45.85,1470655046,List(
      Accelerometer(0.0,-0.1,-1.0,49), Accelerometer(0.0,-0.1,-1.0,149), Accelerometer(0.0,-0.1,-1.0,249),
      Accelerometer(0.0,-0.1,-1.0,349), Accelerometer(-0.1,-0.1,-1.0,449), Accelerometer(-0.1,-0.1,-1.0,553),
      Accelerometer(-0.1,-0.1,-1.0,652), Accelerometer(-0.1,-0.1,-1.0,752), Accelerometer(-0.1,-0.1,-1.0,852),
      Accelerometer(-0.1,-0.1,-1.0,952))
    ),
    TrackPoint(52.4323181156,13.1817902159,45.85,1470655047,List(
      Accelerometer(-0.1,-0.1,-1.0,24), Accelerometer(-0.1,-0.1,-1.0,124), Accelerometer(-0.1,-0.1,-1.0,224),
      Accelerometer(0.0,-0.1,-1.0,324), Accelerometer(0.0,-0.1,-1.0,424), Accelerometer(0.0,-0.1,-1.0,524),
      Accelerometer(0.0,-0.1,-1.0,624), Accelerometer(0.0,-0.1,-1.0,724), Accelerometer(0.0,-0.1,-1.0,824),
      Accelerometer(-0.1,-0.1,-1.0,924))
    ),
    TrackPoint(52.4323180318,13.1817901321,45.85,1470655048,List(
      Accelerometer(-0.1,0.0,-1.0,55), Accelerometer(-0.1,-0.1,-1.0,155), Accelerometer(-0.1,-0.1,-1.0,255),
      Accelerometer(-0.1,0.0,-1.1,355), Accelerometer(0.0,0.0,-1.1,455), Accelerometer(0.0,-0.1,-1.1,555),
      Accelerometer(0.0,-0.1,-1.0,655), Accelerometer(0.0,-0.1,-1.0,755), Accelerometer(0.0,-0.1,-1.0,855),
      Accelerometer(0.0,-0.1,-1.0,955))
    )
  )

  def commonMinimal: List[TrackPoint] = GpxParser.parse(new ByteArrayInputStream(minimalGpxFile.getBytes())).toList

  def commonSwimming: List[TrackPoint] = GpxParser.parse(new ByteArrayInputStream(swimmingGpxFile.getBytes())).toList

  def commonGpxWithAccelerationData: List[TrackPoint] = GpxParser.parse(
    new ByteArrayInputStream(gpxFileWithAcceleration.getBytes())
  ).toList


  "A parsed minimal gpx file with three trkpt elements" should "contain three TrackPoint objects" in {
    val gpxTrackPoints = commonMinimal
    gpxTrackPoints should have length 3
  }

  it should "be equal to three expected results" in {
    val gpxTrackPoints = commonMinimal
    gpxTrackPoints should be(expectedResults)
  }

  "A swimming file with three elements" should "parse correctly" in {
    val gpxTrackPoint = commonSwimming
    gpxTrackPoint should be(expectedResultsSwimmingFile)
  }

  "A gpx file with acceleration data" should "parse correctly" in {
    val gpxTrackPoint = commonGpxWithAccelerationData
    gpxTrackPoint should be(expectedResultsGpxWithAccelerationData)
  }
}