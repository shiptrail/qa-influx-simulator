# QA influx simulator

## Usage

There are two different simulation scenarios available, which can be found in ```src/test/scala/```:

* ```SendRandomClientUpdates```: generates pseudo random client updates (but adheres to the expected basic data scheme;
e.g.: a lat property will always be a Double between -90.0 and +90.0 etc.). This is useful only for pure stress testing.
It might also be useful for smoke testing the backend (e.g. testing its resilience if being flooded with data which is
valid in terms of the data schema but nonsensical for the application).
* ```SendFileBasedClientUpdates```: generates client updates by parsing a recorded gps track file.

### ```SendRandomClientUpdates```

The sbt task ```gatling:testOnly clientupdates.SendRandomClientUpdates``` takes three Parameters:

* ```urlPrefix``` a url (e.g. ```http://localhost:9000/v1```)
* ```numClients``` the number of concurrent clients (aka. sailing boats) to be simulated
* ```sendDelay``` the number of seconds clients wait between sending new data

The Parameters may be set in the ```JAVA_OPTS``` environment variable. This can be done
either system-wide (refer to you OS documentation for details), inside of your IDE (usually
you can edit your run / debug configuration) or (if you use a decent operating systems)
in a shell as follows:

```
$ JAVA_OPTS="-DurlPrefix=http://localhost:9000/v1 -DnumClients=50 -DsendDelay=1" \
sbt "gatling:testOnly SendRandomClientUpdates"
```

### ```SendFileBasedClientUpdates```

The sbt task ```gatling:testOnly SendFileBasedClientUpdates``` takes five Parameters:

* ```urlPrefix``` a url (e.g. ```http://localhost:9000/v1```)
* ```numClients``` the number of concurrent clients (aka. sailing boats) to be simulated
* ```sendDelay``` the number of milli-seconds clients wait between sending new data
* ```batchSize``` the number of clientUpdates to send in one REST request to the backend
* ```fileName``` a path to a file to be parsed (e.g.: ../fe-prototype/tracks/346181868.gpx)

The Parameters may be set in the ```JAVA_OPTS``` environment variable. This can be done
either system-wide (refer to you OS documentation for details), inside of your IDE (usually
you can edit your run / debug configuration) or (if you use a decent operating systems)
in a shell as follows:

```
$ JAVA_OPTS="-DurlPrefix=http://localhost:9000/v1 -DnumClients=5 -DsendInterval=10 -DbatchSize=1 -DfileName=../fe-prototype/tracks/346181868.gpx" \
sbt "gatling:testOnly SendFileBasedClientUpdates"
```

## Output

* Gatling prints out some information about the progress of the simulations on the console
(progress bar, failed requests etc.)
* Gatling also generates nice looking html reports (a path to a html file is printed to
the console as soon as the simulation has ended)

## Current limitations

* ```SendRandomClientUpdates```:
    * The data sent by simulated clients is mostly random (the data adheres to basic rules; if a batch of data
    is sent at once by a client the used id is the same across all data points)
    * Every time a client sends a batch of data it uses a new id
    * The number of data points in one batch (read array) is hard-coded to 10

* ```SendFileBasedClientUpdates```:
    * The only supported file type is GPX
    * IDs are randomly generated and may not be unique (with a low probability)

## Directory contents:

* ```mockup-server```` a play server which will serve as a mockup for the backend (See ```mockup-server/README.md``` for details)
* ```build.sbt``` a build defintion which currently does nothing. We will use this file in the future to run tasks from the mockup-server
as well as from the influx simulator itself.

