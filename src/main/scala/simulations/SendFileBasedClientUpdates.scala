package simulations

import clientupdates._

class SendFileBasedClientUpdates(conf: Conf)
  extends SendClientUpdates(conf)({

    MultiFormatParser
      .parse(conf.file())
      .toList
  })
