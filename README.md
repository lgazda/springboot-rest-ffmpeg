# VideoProbe

springboot + restapi + swagger api + dockerized ffmpeg

## General notes

Application exposes only one endpoint [POST]  
```
/api/videoprobe
```
The endpoint consumes **multipart/form-data** with video file.

File should be send under **file** part and has content type set to **video**
  
# Getting Started
## Prerequisites

* java 8
* ffmpeg (https://ffmpeg.org/download.html)
* docker 

# Configuration
Application settings are located in 
```
src/main/resources/application.properties 
```
By the default videoprobe requires ffprobe executable on the system path, you can set up a custom path by override setting 
``` 
ffmpeg.probe.path=ffprobe
``` 

# Build and Run

## Building with Maven
```
mvn package
```

## How to run  
Please refer to http://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-running-your-application.html 

## Sample request
 ```
 curl --header 'Content-Type: multipart/form-data' --header 'Accept: application/json' -F 'file=@2${videoFilePath};type=video/wvm' localhost:8080/api/videoprobe
 ``` 

 ```
 curl localhost:8080/api/videoprobe
 ``` 
# Docker 
 VideoProbe is dockerized based on image kperson/alpine-java-8-ffmpeg
 Based on default configuration the application is exposed on port: 8080
 
 ## Building docker image
 ```
 mvn docker:build
 ``` 
 
 ## Running docker image
  ``` 
 docker run -p 127.0.0.1:8080:8080 lgazda/videoprobe
 ```
 This binds port 8080 of the container to port 8080 on 127.0.0.1 of the host machine
 
## TODO 
Try to probe file directly from the multipart temp folder 