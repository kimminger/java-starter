 [![CircleCI](https://circleci.com/gh/ElderByte-/spring-cloud-starter-bootstrap.svg?style=svg)](https://circleci.com/gh/ElderByte-/spring-cloud-starter-bootstrap)


## starter  [![Download](https://api.bintray.com/packages/elderbyte/maven/starter/images/download.svg) ](https://bintray.com/elderbyte/maven/starter/_latestVersion)

A java common library to streamline basic java code.

* Common Exceptions
* `ContinuationListing<T>`, `ContinuationToken` Support


## starter-spring-jpa  [![Download](https://api.bintray.com/packages/elderbyte/maven/starter-spring-jpa/images/download.svg) ](https://bintray.com/elderbyte/maven/starter-spring-jpa/_latestVersion)

Provides an initial spring boot / cloud configuration for usage in microservice environments. The configuration is automatically applied when this library is on the class-path.

* Feign Spring Support for common spring data types
* Jackson Spring Support for common spring data types
* Spring MVC generic exception handler for the common exceptions

## starter-spring-mongo  [![Download](https://api.bintray.com/packages/elderbyte/maven/starter-spring-mongo/images/download.svg) ](https://bintray.com/elderbyte/maven/starter-spring-mongo/_latestVersion)

Provides utilities and converters for spring mongo db.

* OffsetDateTime and ZonedDateTime converters
* Native Text Query utilities

### Version

Since this library attempts to support spring, specifically spring boot, the versioning follows spring boot closely:

`{library-version}-Sp{spring-boot-version}`
For example, the version `1.0.0-Sp2.0.1` means that the library is meant for spring boot `2.0.1` and its the version '1.0.0' of this library.

# Usage

Simply add this library as a dependency to your project. It will be automatically picked up by springs auto configuration.


