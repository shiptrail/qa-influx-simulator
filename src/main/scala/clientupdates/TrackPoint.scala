package clientupdates

case class Accelerometer(x: Double, y: Double, z: Double, toffset: Int)

case class TrackPoint(lat: Double, lng: Double, ele: Double, time: Int, accelerometer: Seq[Accelerometer])