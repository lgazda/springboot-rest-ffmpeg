package pl.net.gazda.videoprobe.domain.configuration;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static pl.net.gazda.videoprobe.domain.VideoProbeResultFactory.randomFFmpegProbeResult;

@Configuration
public class FFProbeConfiguration {
    @Value("${ffmpeg.probe.path:ffprobe}")
    private String probeRunnablePath;

    @Value("${ffmpeg.probe.mock.enabled:false}")
    private boolean mockFFProbe;

    @Bean
    public FFprobe ffprobe() throws IOException {
        return mockFFProbe ? mockFFProbe() : genuineFFProbe();
    }

    private FFprobe mockFFProbe() throws IOException {
        return new FFprobe() {
            @Override
            public FFmpegProbeResult probe(String mediaPath) throws IOException {
                return randomFFmpegProbeResult();
            }
        };
    }

    private FFprobe genuineFFProbe() throws IOException {
        FFprobe fFprobe = new FFprobe(probeRunnablePath);
        fFprobe.isFFprobe();
        return fFprobe;
    }
}
