package pl.net.gazda.videoprobe.web;

import io.swagger.annotations.*;
import pl.net.gazda.videoprobe.domain.RestApiResponse;
import pl.net.gazda.videoprobe.domain.VideoProbeResult;
import pl.net.gazda.videoprobe.service.VideoProbeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import static pl.net.gazda.videoprobe.web.MultipartVideoFileValidator.VIDEO_TYPE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * curl --header 'Content-Type: multipart/form-data' --header 'Accept: application/json' -F 'file=@20mb.mp4;type=video/wvm' localhost:8080/api/videoprobe
 */
@ApiController
@Api(description="Operation for video probing.", tags = {"videoprobe"})
public class VideoProbeController {
    private final VideoProbeService videoProbeService;
    private final MultipartVideoFileValidator validator;

    @Autowired
    public VideoProbeController(VideoProbeService videoProbeService, MultipartVideoFileValidator validator) {
        this.videoProbeService = videoProbeService;
        this.validator = validator;
    }

    /**
     * Post and multipart/form-data for browser/js compatibility.
     */
    @ApiOperation(value = "Probes a video file and returns basic video information.", response = VideoProbeResult.class, produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful video probe", response = VideoProbeResult.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 405, message = "Wrong http method was used"),
            @ApiResponse(code = 406, message = "For not acceptable content type"),
            @ApiResponse(code = 415, message = "Wrong main request mime type"),
            @ApiResponse(code = 500, message = "Internal server error", response = RestApiResponse.class)})
    @PostMapping(path = "videoprobe",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE,
            headers = "Accept=application/json")
    public VideoProbeResult probeVideo(@ApiParam(value = "video file to probe", required = true)
                                       @RequestParam("file") MultipartFile file) throws Exception {
        validator.requiresMediaType(file, VIDEO_TYPE);
        return videoProbeService.probe(file.getInputStream());
    }
}
