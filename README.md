# QA influx simulator

## Known issues
* The parser methods are not thread-safe! The exact reason for this is 
 unknown. Any use of a parse method in parallel with another parse method
 (even if it is two different parsers) may return corrupted data!
 This is why the test suite is currently executed in sequential mode only.
 However, you may execute as many INSI instances (read: INSI processes) 
 as you like in parallel. 

## Prerequisites

Mandatory:
* Java7 or higher
* sbt

Required for parsing FIT files:
* ```gpsbabel``` available in your $PATH (on windows in your %PATH%)
```gpsbabel``` is also required if you want to execute the test suite.

For instructions on how to install ```gpsbabel``` in your operating system
see the individual sub-folders in ```dist/```.

## Usage

After installation (see below Building Packages) a executable `insi` is available.

To execute the same task with sbt without packaging and installing the executable use `sbt run`.
E.g. `sbt "run -c 50 --batch-size 1 track.gpx"` instate of `insi -c 50 --batch-size 1 tack.gpx`.
The quotation marks are important for sbt.

To start the application from the sbt console use the same command as above, but without quotation marks.

Use `insi --help` or `sbt "run --help"` to see all available options.

# Development

Run tests: ```$ sbt test```

Create test coverage report: ```$ sbt coverage test coverageReport```. (Will fail if the coverage is to low or some
tests fails) The generated HTML report is available under target/scala-2.11/scoverage-report/index.html (also mentioned
in the console output)

# Building packages

Insi can be run like a regular command line application without sbt like:

```
$ insi ../foobar.fit
```
 
In order to accomplish this you may generate debian, rpm or zip packages and
install these packages on your system.

## Debian packages

Execute:

```$ sbt clean debian:package-bin```

Afterwards a deb file is located in the ```target``` directory.

## Rpm packages

IMPORTANT: you need ```rpm``` and ```rpmbuild``` in your path in order
to build rpm packages.

Execute:

```$ sbt clean rpm:package-bin```

Afterwards a rpm file is located in the ```target/rpm/RPMS/noarch``` 
directory.

## Zip packages

Execute:

```$ sbt clean universal:package-bin```

Afterwards a zip file is located in the ```target/universal``` 
directory.

You may unpack the generated zip file and add the contained ```bin``` 
directory to your ```$PATH``` (or ```%PATH%``` on windows)
