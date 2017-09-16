#  NumberToEnglish

A library that supports numeric value formatting to english words.

To consume this library see the project page here.

[https://505aaron.github.io/NumberToEnglish](https://505aaron.github.io/NumberToEnglish/index.html)

## Building
`mvn clean install`

## CLI Tester

### Build a single JAR
`mvn clean compile assembly:single`

### Run the CLI
`jar -jar target/NumberToEnglish-1.0-SNAPSHOT-jar-with-dependencies.jar -v 10000`

## Sonar

Ensure that your local maven settings are configured to your sonar server.
[Sonar Instructions](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Maven)

`mvn clean verify sonar:sonar`

## Build Documentation Site

`mvn clean`

`git worktree add -B gh-pages site origin/gh-pages`

`mvn package site && \cp -rf target/site .`

`cd site && git add --all && git commit -nm "Publishing to gh-pages" && cd ..`

`git push origin gh-pages`