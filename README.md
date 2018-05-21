# nannmon (難問)

## Description:

Backend challenge for N26: Create 2 api's for storing and retrieving real-time transaction statitics for the most recent 60 second period. Storage cannot use a db and must be thread-safe. Api's should aim for constant time and space performance.

## Prerequisites:

### Requires:
- Java8
- gradle 4.7+
- lombok 1.16.20

### Instructions:

I've checked in the gradle wrapper so if you are unsure of how to install gradle, please use the wrapper (./gradlew or ./gradlew.bat) command.

Lombok installation is only necessary if you'd like to load the source code up in an IDE, please run './gradlew installLombok' to install the correct version of lombok to your IDE. For further instructions please read the installation instructions at https://projectlombok.org/.

## Design Details:

For performance reasons transaction values are stored in a 60 element array, where each element will store 1 seconds worth of transaction data. The array will loop over itself so we will only ever store the most recent 60 seconds worth of data.  Read/Write operations on the array element object (AggregateStatistic) have been locked so operations will be thread-safe. Old data (ie; the next element in the array) is cleaned up every second by a scheduled job before new data is written to it. 

## Execution:

To run the tests:
./gradlew test

To run the application:
./gradlew bootRun

### Notes: 
- test results can be viewed after the tests have been run at: build/reports/tests/test/index.html
- application will start up on port 8080
- logging is fairly verbose, please configure log level in application.properties to your liking
