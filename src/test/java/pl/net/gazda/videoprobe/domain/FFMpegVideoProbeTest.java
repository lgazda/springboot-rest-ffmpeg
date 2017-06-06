package pl.net.gazda.videoprobe.domain;

import net.bramp.ffmpeg.FFprobe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.net.gazda.videoprobe.domain.FFMpegVideoProbe.VideoProbeException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static pl.net.gazda.videoprobe.test.TestUtils.anyFFmpegProbeResult;
import static pl.net.gazda.videoprobe.test.TestUtils.anyPath;

@RunWith(MockitoJUnitRunner.class)
public class FFMpegVideoProbeTest {
    @Mock
    private FFprobe ffprobe;
    @Mock
    private VideoProbeResultFactory factory;

    private FFMpegVideoProbe testObject;

    @Before
    public void before() {
        testObject = new FFMpegVideoProbe(ffprobe, factory);
    }

    @Test
    public void should_throwVideoProbeException_when_fFprobeThrowsAnException() throws IOException, VideoProbeException {
        given(ffprobe.probe(any())).willThrow(new RuntimeException("TEST"));
        assertThatThrownBy(() -> testObject.probeVideoFile(anyPath()))
                .isInstanceOf(VideoProbeException.class);
    }

    @Test
    public void should_throwVideoProbeException_when_factoryThrowsException() throws IOException, VideoProbeException {
        given(ffprobe.probe(any())).willReturn(anyFFmpegProbeResult());
        given(factory.from(any())).willThrow(new RuntimeException("TEST"));
        assertThatThrownBy(() -> testObject.probeVideoFile(anyPath()))
                .isInstanceOf(VideoProbeException.class);
    }

}