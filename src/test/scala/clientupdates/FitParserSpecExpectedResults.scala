package clientupdates

trait FitParserSpecExpectedResults {
  val expectedResultsFirst: List[TrackPoint] = List(
    TrackPoint(52.505640263, 13.209531556, 53.8, 1378895959, Seq.empty), TrackPoint(52.505621571, 13.209497944, 38.0, 1378895961, Seq.empty)
  )

  val expectedResultsLast: List[TrackPoint] = List(
    TrackPoint(52.505771104, 13.209223185, 39.0, 1378906111, Seq.empty), TrackPoint(52.505831622, 13.209247912, 39.0, 1378906114, Seq.empty)
  )
}
