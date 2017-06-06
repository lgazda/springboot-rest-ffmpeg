package pl.net.gazda.videoprobe.domain;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FFMpegVideoProbe {
    private final FFprobe ffprobe;
    private final VideoProbeResultFactory resultFactory;

    @Autowired
    public FFMpegVideoProbe(FFprobe ffprobe, VideoProbeResultFactory resultFactory) {
        this.ffprobe = ffprobe;
        this.resultFactory = resultFactory;
    }

    public VideoProbeResult probeVideoFile(Path path) throws VideoProbeException {
        try {
            FFmpegProbeResult probeResult = ffprobe.probe(path.toString());
            return resultFactory.from(probeResult);
        } catch (Exception e) {
            throw new VideoProbeException(e);
        }
    }

    public static class VideoProbeException extends Exception {
        public VideoProbeException(Throwable ex) {
            super(ex);
        }

        @Override
        public String toString() {
            return "Unable to probe video file";
        }
    }
}
