# videoprobe
springboot + restapi + swagger api + dockerized ffmpeg video probing

# configuration

by default videoprobe requires ffprobe on system path, you can override this by setting 'ffmpeg.probe.path' property
> ffmpeg.probe.path=
>

# build 

# dockerized
 docker image is based on kperson/alpine-java-8-ffmpeg
 
 application exposes on port: 8080
 
 run docker image with published port '
 > docker run -p 127.0.0.1:8080:8080 lgazda/videoprobe
 
 This binds port 8080 of the container to port 8080 on 127.0.0.1 of the host machine
 
 # how to
  Application exposes one POST endpoint. 
    /api/videoprobe
  endpoint consumes multipart/form-data with video file send under 'file' part. 
  enpoint accepts only multipart file with be mime type 'video/*'
  
  sample request:
 > curl --header 'Content-Type: multipart/form-data' --header 'Accept: application/json' -F 'file=@20mb.mp4;type=video/wvm' localhost:8080/api/videoprobe
 
