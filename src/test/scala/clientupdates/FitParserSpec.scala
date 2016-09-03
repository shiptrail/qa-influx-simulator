package clientupdates

import java.net.URL

import org.scalatest.{FlatSpec, Matchers}

class FitParserSpec extends FlatSpec with Matchers {
  val minimalFitFile = getClass.getResource("/fixtures/track.fit")

  def commonFit: List[TrackPoint] = FitParser.parse(minimalFitFile).toList

  val expectedResultsminimalFitFirstTwo = List(
    TrackPoint(52.505640263,13.209531556,53.8,1378895959, Seq.empty),
    TrackPoint(52.505621571,13.209497944,38.0,1378895961, Seq.empty)
  )

  val expectedResultsminimalFitLastTwo = List(
    TrackPoint(52.505771104,13.209223185,39.0,1378906111, Seq.empty),
    TrackPoint(52.505831622,13.209247912,39.0,1378906114, Seq.empty)
  )

  "parse(url: URL)" should "return an empty iterator for an unknown file" in {
    val url = new URL("file:///foooooobaaaarrrrrrrrrrrr.fit")
    val trackPoints = FitParser.parse(url)
    trackPoints should be(Iterator.empty)
  }

  "the FIT file in the resources folder" should "contain two well know track points at the start/end of the list" in {
    val trackPoints = commonFit
    trackPoints.take(2) should be(expectedResultsminimalFitFirstTwo)
    trackPoints.takeRight(2) should be(expectedResultsminimalFitLastTwo)
  }
}
