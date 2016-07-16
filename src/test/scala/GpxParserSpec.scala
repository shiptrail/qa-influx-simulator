import java.io.ByteArrayInputStream

import clientupdates.{GpxParser, _}
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

  val expectedResults = List(
    TrackPoint(52.508182134479284, 13.209660965949297, 20.399999618530273, 1378123000),
    TrackPoint(52.508178278803825, 13.209655852988362, 24.0, 1378123001),
    TrackPoint(52.50816285610199, 13.209633054211736, 24.0, 1378123007)
  )

  val expectedResultsSwimmingFile = List(
    TrackPoint(52.509451,13.209473,36.0,1374327451),
    TrackPoint(52.509423,13.209413,36.0,1374327483),
    TrackPoint(52.509328,13.209443,35.4,1374328749),
    TrackPoint(52.509415,13.209395,34.9,1374328786)
  )

  def commonMinimal: List[TrackPoint] = GpxParser.parse(new ByteArrayInputStream(minimalGpxFile.getBytes())).toList

  def commonSwimming: List[TrackPoint] = GpxParser.parse(new ByteArrayInputStream(swimmingGpxFile.getBytes())).toList


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
}