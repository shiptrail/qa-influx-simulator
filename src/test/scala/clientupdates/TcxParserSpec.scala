package clientupdates

import java.io.ByteArrayInputStream

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by phiros on 18.07.16.
  */
class TcxParserSpec extends FlatSpec with Matchers {
  val minimalTcxFile =
    """
      |<TrainingCenterDatabase
      |  xsi:schemaLocation="http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd"
      |  xmlns:ns5="http://www.garmin.com/xmlschemas/ActivityGoals/v1"
      |  xmlns:ns3="http://www.garmin.com/xmlschemas/ActivityExtension/v2"
      |  xmlns:ns2="http://www.garmin.com/xmlschemas/UserProfile/v2"
      |  xmlns="http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2"
      |  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ns4="http://www.garmin.com/xmlschemas/ProfileExtension/v1">
      |  <Activities>
      |    <Activity Sport="Other">
      |      <Id>2012-09-24T07:12:30.000Z</Id>
      |      <Lap StartTime="2012-09-24T07:12:30.000Z">
      |        <TotalTimeSeconds>357.732</TotalTimeSeconds>
      |        <DistanceMeters>1000.0</DistanceMeters>
      |        <MaximumSpeed>2.914000034332275</MaximumSpeed>
      |        <Calories>96</Calories>
      |        <Intensity>Active</Intensity>
      |        <TriggerMethod>Manual</TriggerMethod>
      |        <Track>
      |          <Trackpoint>
      |            <Time>2012-09-24T07:12:30.000Z</Time>
      |            <Position>
      |              <LatitudeDegrees>52.508397717028856</LatitudeDegrees>
      |              <LongitudeDegrees>13.210077797994018</LongitudeDegrees>
      |            </Position>
      |            <AltitudeMeters>36.599998474121094</AltitudeMeters>
      |            <DistanceMeters>0.8999999761581421</DistanceMeters>
      |            <Extensions>
      |              <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
      |                <Speed>2.2360000610351562</Speed>
      |              </TPX>
      |            </Extensions>
      |          </Trackpoint>
      |          <Trackpoint>
      |            <Time>2012-09-24T07:12:35.000Z</Time>
      |            <Position>
      |              <LatitudeDegrees>52.50828489661217</LatitudeDegrees>
      |              <LongitudeDegrees>13.210050472989678</LongitudeDegrees>
      |            </Position>
      |            <AltitudeMeters>36.599998474121094</AltitudeMeters>
      |            <DistanceMeters>13.630000114440918</DistanceMeters>
      |            <Extensions>
      |              <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
      |                <Speed>2.4539999961853027</Speed>
      |              </TPX>
      |            </Extensions>
      |          </Trackpoint>
      |          <Trackpoint>
      |            <Time>2012-09-24T07:12:42.000Z</Time>
      |            <Position>
      |              <LatitudeDegrees>52.50811725854874</LatitudeDegrees>
      |              <LongitudeDegrees>13.210010407492518</LongitudeDegrees>
      |            </Position>
      |            <AltitudeMeters>36.599998474121094</AltitudeMeters>
      |            <DistanceMeters>32.5099983215332</DistanceMeters>
      |            <Extensions>
      |              <TPX xmlns="http://www.garmin.com/xmlschemas/ActivityExtension/v2">
      |                <Speed>2.575000047683716</Speed>
      |              </TPX>
      |            </Extensions>
      |          </Trackpoint>
      |        </Track>
      |      </Lap>
      |    </Activity>
      |  </Activities>
      |</TrainingCenterDatabase>
      |""".stripMargin

  def commonTcx: List[TrackPoint] = TcxParser.parse(new ByteArrayInputStream(minimalTcxFile.getBytes())).toList

  val expectedResultsminimalTcx = List(
    TrackPoint(52.508397717028856,13.210077797994018,1348463550,Some(36.599998474121094)),
    TrackPoint(52.50828489661217,13.210050472989678,1348463555,Some(36.599998474121094)),
    TrackPoint(52.50811725854874,13.210010407492518,1348463562,Some(36.599998474121094))
  )

  "A parsed minimal tcx file with three Trackpoint elements" should "contain three TrackPoint objects" in {
    val trackPoints = commonTcx
    trackPoints should have length 3
  }

  it should "contain the expected TrackPoints" in {
    val trackPoints = commonTcx
    trackPoints should be(expectedResultsminimalTcx)
  }
}
