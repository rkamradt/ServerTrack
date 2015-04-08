# ServerTrack
A sample web-service that will track server statistics over time

This is a standard Maven Multi-module build. It requires Java 8 and Maven 3.2.5

To build and run unit tests:

```
mvn install
```

To build and run integration tests:

```
mvn verify
```
The spec for this project consists of:

 1. Record load for a given server
 This should take a:
    server name (string)
    CPU load (double)
    RAM load (double)
 And apply the values to an in-memory model used to provide the data in endpoint #2.

 2. Display loads for a given server
 This should return data (if it has any) for the given server:
    A list of the average load values for the last 60 minutes broken down by minute
    A list of the average load values for the last 24 hours broken down by hour

Question for requirement source:
 1. can the number of minutes and hours be modified? consider this possibility for increase opportunity for code-reuse.
 2. are the time-buckets for minutes and hours aligned with actual minutes and hours or relative to the time of the request? I used relative to simplify and to make the first bucket a full minute/hour

The project consists of three modules: two jar and one war. The two jar files are for a SPI and a reference implementation. The war file implements a restful web-service. The reference implementation uses a simple memory store that is lost whenever the service is restarted. The war is deployable to Tomcat 7 and the integration tests are run in Tomcat 7. Deployment to any other app server may be possible, but is untested.

The service implements two endpoints: GET and POST. The name of a server is appended to the path. The POST takes data in the form of:

{
  cpuLoad: {double},
  ramLoad: {double}
}

The GET returns data in the form of:

{
  serverName: {String},
  byHour: [ {cpuLoad: {double}, ramLoad: {double} }],
  byMinute: [ {cpuLoad: {double}, ramLoad: {double} }]
}

The data returned from GET is normalized into 24 hourly buckets and 60 minute buckets.
