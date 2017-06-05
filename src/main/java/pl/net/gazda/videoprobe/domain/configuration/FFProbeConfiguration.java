package pl.net.gazda.videoprobe.domain.configuration;

import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FFProbeConfiguration {
    @Value("${ffmpeg.probe.path:ffprobe}")
    private String probeRunnablePath;

    @Bean
    public FFprobe ffprobe() throws IOException {
        FFprobe fFprobe = new FFprobe(probeRunnablePath);
        fFprobe.isFFprobe();
        return fFprobe;
    }
}
