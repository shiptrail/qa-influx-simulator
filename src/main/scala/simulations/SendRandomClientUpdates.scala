package simulations

import clientupdates.{ClientUpdate, Conf}
import org.scalacheck._

class SendRandomClientUpdates(conf: Conf)
    extends SendClientUpdates(conf)({

      lazy val genClientUpdate = for {
        lat <- Gen.choose[Double](-90.0, 90.0)
        lng <- Gen.choose[Double](-180.0, 180.0)
        ele <- Gen.choose[Double](-100.0, 200.0)
        heading <- Gen.choose[Double](0.0, 360.0)
        timestamp <- Gen.choose[Int](0, 9999999)
      } yield ClientUpdate(lat, lng, ele, heading, timestamp)

      lazy val clientUpdates: List[ClientUpdate] =
        Gen.listOfN(randomTrackListLength, genClientUpdate).sample.get

      def randomTrackListLength = 500

      clientUpdates
    })
