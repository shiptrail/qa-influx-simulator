set JAVA_OPTS=-DurlPrefix=http://localhost:9000/v2 -DnumClients=1 -DsendDelay=1 -DbatchSize=100 -DfileName=../346181868.gpx
sbt "gatling:testOnly simulations.SendFileBasedClientUpdates"