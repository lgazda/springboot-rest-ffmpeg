package pl.net.gazda.videoprobe.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.net.gazda.videoprobe.domain.FFMpegVideoProbe;
import pl.net.gazda.videoprobe.domain.VideoProbeResult;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.properties"}, properties = {"ffmpeg.probe.mock.enabled=false"})
@Ignore("This test requires ffprobe installed")
public class VideoProbeServiceTest {
    @Autowired
    private VideoProbeService probeService;
    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void should_returnVideoProbeResult_whenFileInputStreamIsUsed() throws IOException, FFMpegVideoProbe.VideoProbeException {
        VideoProbeResult result = probeService.probe(sampleVideoFile().getInputStream());
    }

    private Resource sampleVideoFile() {
        return resourceLoader.getResource("classpath:samples/SampleVideo_1280x720_1mb.mp4");
    }
}