package pl.net.gazda.videoprobe.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.net.gazda.videoprobe.domain.VideoProbeResult;
import pl.net.gazda.videoprobe.domain.VideoProbeResult.Codec;
import pl.net.gazda.videoprobe.domain.VideoProbeResult.VideoFormat;
import pl.net.gazda.videoprobe.service.VideoProbeService;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.net.gazda.videoprobe.domain.VideoProbeResult.CodecType.AUDIO;
import static pl.net.gazda.videoprobe.domain.VideoProbeResult.CodecType.VIDEO;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@TestPropertySource(locations = {"classpath:application-test.properties"})
public class VideoProbeControllerTest {
    private static final String API_VIDEOPROBE_PATH = "/api/videoprobe";
    @MockBean
    private VideoProbeService videoProbeService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * more test here ...
     */

    @Test
    public void should_returnVideoResultJSONResponse_when_validPostMultipartRequest() throws Exception {
        given(videoProbeService.probe(any()))
            .willReturn(videoProbeResult());

        mockMvc.perform(validVideoProbeRequest())
            .andExpect(status().is(OK.value()))
            .andExpect(jsonContentType())
            .andExpect(jsonPath("$.*", hasSize(2)))
            .andExpect(jsonPath("$.codecs", hasSize(2)))
            .andExpect(jsonPath("$.codecs[*].type", containsInAnyOrder("VIDEO", "AUDIO")))
            .andExpect(jsonPath("$.codecs[*].bitrate", containsInAnyOrder(1000, 2000)))
            .andExpect(jsonPath("$.codecs[*].name", containsInAnyOrder("TC1", "TC2")))
            .andExpect(jsonPath("$.codecs[*].longName", containsInAnyOrder("Test Codec 1", "Test Codec 2")))
            .andExpect(jsonPath("$.video.*", hasSize(3)))
            .andExpect(jsonPath("$.video.duration", equalTo(12000D)))
            .andExpect(jsonPath("$.video.size", equalTo(10000)))
            .andExpect(jsonPath("$.video.bitrate", equalTo(12000)));
    }

    @Test
    public void should_return406_when_requestAcceptNotSetToApplicationJSON() throws Exception {
        mockMvc.perform(validVideoProbeRequest().accept(MediaType.APPLICATION_PDF))
            .andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()));
    }

    @Test
    public void should_return404_when_multipartRequestWithWrongPartName() throws Exception {
        mockMvc.perform(validVideoProbeFileRequestWith(fileWithWrongPartName()))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonContentType());
        //check jsonPath here
    }

    @Test
    public void should_return415_when_requestIsNotMultiPart() throws Exception {
        mockMvc.perform(validVideoProbeRequest().contentType(MediaType.IMAGE_GIF))
            .andExpect(status().is(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
            .andExpect(jsonContentType());
        //check jsonPath here
    }

    @Test
    public void should_return415_when_fileIsNotVideoContentType() throws Exception {
        mockMvc.perform(noVideoFileProbeRequest())
            .andExpect(status().is(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
            .andExpect(jsonContentType());
        //check jsonPath here
    }

    private RequestBuilder validVideoProbeRequestWithBigContent() {
        byte[] content = new byte[2049]; content[1024] = 111;
        MockMultipartFile file = new MockMultipartFile("file", "filename.mp4", "video/mp4", content);
        return validVideoProbeFileRequestWith(file);
    }

    private MockMultipartFile fileWithWrongPartName() {
        return new MockMultipartFile("fileWrongPartName", "filename.mp4", "video/mp4", "123".getBytes());
    }

    private ResultMatcher jsonContentType() {
        return content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON);
    }

    private VideoProbeResult videoProbeResult() {
        return new VideoProbeResult(
                new VideoFormat(12000d, 10000L, 12000L),
                    asList(new Codec("TC1", "Test Codec 1", 1000L, VIDEO), new Codec("TC2", "Test Codec 2", 2000L, AUDIO)));
    }

    private MockHttpServletRequestBuilder validVideoProbeRequest() {
        return validVideoProbeFileRequestWith(sampleFile("video/mp4"));
    }

    private MockHttpServletRequestBuilder noVideoFileProbeRequest() {
        return validVideoProbeFileRequestWith(sampleFile("videoX/mp4"));
    }

    private MockHttpServletRequestBuilder validVideoProbeFileRequestWith(MockMultipartFile file) {
        return videoProbePost().file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockMultipartHttpServletRequestBuilder videoProbePost() {
        return MockMvcRequestBuilders.fileUpload(API_VIDEOPROBE_PATH);
    }

    private MockMultipartFile sampleFile(String contentType) {
        return new MockMultipartFile("file", "filename.mp4", contentType, "fake".getBytes());
    }
}