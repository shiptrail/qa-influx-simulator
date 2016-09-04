package clientupdates

trait FitParserSpecExpectedResults {
  val expectedResultsFirst: List[TrackPoint] = List(
    TrackPoint(52.505640263, 13.209531556, 1378895959, Some(53.8)), TrackPoint(52.505621571, 13.209497944,
    1378895961, Some(38.0))
  )

  val expectedResultsLast: List[TrackPoint] = List(
    TrackPoint(52.505771104, 13.209223185, 1378906111, Some(39.0)), TrackPoint(52.505831622, 13.209247912,
    1378906114, Some(39.0))
  )
}
