### Native Image Builder.

* **Author**: [Andres Solorzano](https://www.linkedin.com/in/aosolorzano/).
* **Level**: 300 - Intermediate.
* **Technologies**: Docker.

### What is it?
This is a simple docker project that builds a base image which contains the native-image tool from GraalVM for Java 21. 
This image is used to build native images from Java applications in the Hiperium City project.

### System requirements
All you need to build this project is Docker.

### How to use it?
To build the image, you can run the following command:
```bash
docker build -t native-image-builder:1.0 .
```

### How to test it?
To test the image, you can run the following command:
```bash
docker run -it --rm native-image-builder:1.0
```

### How to extend it?
You can extend this image to include your Java application and build a native image from it.

### How to deploy it to Docker Hub?
To deploy the image to Docker Hub, you can run the following commands:
```bash
docker login
docker tag native-image-builder:1.0 hiperium/native-image-builder:1.0
docker push hiperium/native-image-builder:1.0
```
