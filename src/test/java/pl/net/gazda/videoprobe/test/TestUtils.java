package pl.net.gazda.videoprobe.test;

import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import pl.net.gazda.videoprobe.domain.VideoProbeResult;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static pl.net.gazda.videoprobe.domain.VideoProbeResultFactory.randomFFmpegProbeResult;

public final class TestUtils {


    public static Path anyPath() {
        return Paths.get(String.format("%s/%s/%s", randomAlphabetic(5), randomAlphabetic(3), randomAlphabetic(4)));
    }

    public static FFmpegProbeResult anyFFmpegProbeResult() {
        return randomFFmpegProbeResult();
    }

    public static VideoProbeResult anyProbeResult() {
        return new VideoProbeResult(null, emptyList());
    }
}
