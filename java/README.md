## Common classes and Utilities for the Hiperium City project.

* **Author**: [Andres Solorzano](https://www.linkedin.com/in/aosolorzano/).
* **Level**: 200 - Basic.
* **Technologies**: Java, Maven.

### What is it?
The Hiperium City Commons contains useful POJO classes and utilities.
This project also contains DTOs, bean validation, enumerations, and other useful classes.
It also contains parent POMs and other configurations to be shared between the Hiperium City services.

### System requirements
All you need to build this project is Java 21 or later.

### Deploying to Maven Central
Execute the following command from inside each Maven project to deploy the artifact to Maven Central:
```bash
mvn clean deploy
```

You will be asked for your GPG passphrase before the deployment to Maven Central.
A deployment will be created in your [Maven Central Repository](https://central.sonatype.com/publishing) account.
You must publish the deployment manually, so the artifact will be available to the public in a couple of minutes.
