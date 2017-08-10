 [ ![Download](https://api.bintray.com/packages/elderbyte/maven/spring-cloud-starter-bootstrap/images/download.svg) ](https://bintray.com/elderbyte/maven/spring-cloud-starter-bootstrap/_latestVersion)


# spring-cloud-starter-bootstrap
Provides an initial spring boot / cloud configuration for usage in microservice environments. The configuration is automatically applied when this library is on the class-path.

## Specifically it provides the following features:

* Commonly used exceptions
* Feign Spring Support for common spring data types
* Jackson Spring Support for common spring data types
* Spring MVC generic exception handler for the common exceptions

# Usage

Simply add this library as a dependency to your project. It will be automatically picked up by springs auto configuration.

## Version

Since this library attempts to support spring, specifically spring boot, the versioning follows spring boot closely:

`{spring-boot-version}.E{library-version}`
For example, the version `1.5.6.E15` means that the library is meant for spring boot `1.5.6` and its the version '15' of this library (for 1.5.6 of spring boot).
