# QA influx simulator

## Usage

The sbt task ```gatling:test``` takes three Parameters:

* ```urlPrefix``` a url (e.g. ```http://localhost:9000/v1```)
* ```numClients``` the number of concurrent clients (aka. sailing boats) to be simulated
* ```sendDelay``` the number of seconds clients wait between sending new data

The Parameters may be set in the ```JAVA_OPTS``` environment variable. This can be done
either system-wide (refer to you OS documentation for details), inside of your IDE (usually
you can edit your run / debug configuration) or (if you use a decent operating systems)
in a shell as follows:

```
$ JAVA_OPTS="-DurlPrefix=http://localhost:9000/v1 -DnumClients=50 -DsendDelay=1" sbt gatling:test
```

## Output

* Gatling prints out some information about the progress of the simulations on the console
(progress bar, failed requests etc.)
* Gatling also generates nice looking html reports (a path to a html file is printed to
the console as soon as the simulation has ended)

## Current limitations

* The data sent by simulated clients is mostly random (the data adheres to basic rules; if a batch of data
is sent at once by a client the used id is the same across all data points)
* Every time a client sends a batch of data it uses a new id
* The number of data points in one batch (read array) is hard-coded to 10


## Directory contents:

* ```mockup-server``` a play server which will serve as a mockup for the backend (See ```mockup-server/README.md``` for details)
* ```build.sbt``` a build definition which currently does nothing. We will use this file in the future to run tasks from the mockup-server
as well as from the influx simulator itself.

# Development

Run tests: ```$ sbt test```

Create test coverage report: ```$ sbt coverage test coverageReport```. (Will fail if the coverage is to low or some
tests fails) The generated HTML report is available under target/scala-2.11/scoverage-report/index.html (also mentioned
in the console output)
