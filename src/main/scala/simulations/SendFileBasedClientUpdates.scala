package simulations

import clientupdates._

class SendFileBasedClientUpdates(conf: Conf)
  extends SendClientUpdates(conf)({

    MultiFormatParser
      .parse(conf.file())
      .map { trackPoint =>
        ClientUpdate(trackPoint.lat,
          trackPoint.lng,
          trackPoint.ele,
          0,
          trackPoint.time,
          trackPoint.accelerometer)
      }
      .toList
  })
