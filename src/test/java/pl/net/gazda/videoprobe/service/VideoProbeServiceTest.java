package pl.net.gazda.videoprobe.service;

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
import pl.net.gazda.videoprobe.domain.VideoProbeResult.Codec;
import pl.net.gazda.videoprobe.domain.VideoProbeResult.VideoFormat;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.net.gazda.videoprobe.domain.VideoProbeResult.CodecType.AUDIO;
import static pl.net.gazda.videoprobe.domain.VideoProbeResult.CodecType.VIDEO;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.properties"}, properties = {"ffmpeg.probe.mock.enabled=false"})
/*
@Ignore("This test requires ffprobe installed")
*/
public class VideoProbeServiceTest {
    @Autowired
    private VideoProbeService probeService;
    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void should_returnVideoProbeResult_whenFileInputStreamIsUsed() throws IOException, FFMpegVideoProbe.VideoProbeException {
        VideoProbeResult result = probeService.probe(sampleVideoFile().getInputStream());
        assertThat(result).isNotNull();
        assertThat(result.getVideoFormat())
                .isEqualToComparingFieldByField(new VideoFormat(5.312, 1055736, 1589963));
        assertThat(result.getCodecs()).hasSize(2);
        assertThat(result.getCodec(AUDIO))
                .hasValueSatisfying(codec -> assertThat(codec).isEqualToComparingFieldByField(
                        new Codec("aac", "AAC (Advanced Audio Coding)", 384828L, AUDIO)));
        assertThat(result.getCodec(VIDEO))
                .hasValueSatisfying(codec -> assertThat(codec).isEqualToComparingFieldByField(
                        new Codec("h264", "H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10", 1205959L, VIDEO)));
    }

    private Resource sampleVideoFile() {
        return resourceLoader.getResource("classpath:samples/SampleVideo_1280x720_1mb.mp4");
    }
}