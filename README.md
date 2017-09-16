#  NumberToEnglish

A library that supports numeric value formatting to english words.

To consume this library see the project page here.

## Building
`mvn clean install`

## Sonar

Ensure that your local maven settings are configured to your sonar server.
[Sonar Instructions](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Maven)

`mvn clean verify sonar:sonar`

## Build Documentation Site

`mvn package site`

## CLI Tester

### Build a single JAR
`mvn clean compile assembly:single`

### Run the CLI
`jar -jar target/NumberToEnglish-1.0-SNAPSHOT-jar-with-dependencies.jar -v 10000`