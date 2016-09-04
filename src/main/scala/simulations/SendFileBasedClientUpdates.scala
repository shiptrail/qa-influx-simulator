package simulations

import clientupdates._

class SendFileBasedClientUpdates(conf: Conf)
  extends SendClientUpdates(conf)({

    MultiFormatParser
      .parse(conf.file())
      .map { trackPoint =>
        TrackPoint(trackPoint.lat,
          trackPoint.lng,
          trackPoint.timestamp,
          trackPoint.ele,
          trackPoint.gpsMeta,
          trackPoint.compass,
          trackPoint.accelerometer,
          trackPoint.orientation)
      }
      .toList
  })
