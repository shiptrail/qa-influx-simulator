set JAVA_OPTS=-DurlPrefix=http://localhost:9000/v2 -DnumClients=1 -DsendDelay=1 -DbatchSize=100
sbt "gatling:testOnly SendRandomClientUpdates"