package pl.net.gazda.videoprobe.domain;

import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class VideoProbeResultFactory {
    public VideoProbeResult from(FFmpegProbeResult probeResult) {
        VideoProbeResult.VideoFormat format = createVideoFormat(probeResult.getFormat());
        List<VideoProbeResult.Codec> codecs = probeResult.getStreams()
                .stream()
                .map(this::createCodec)
                .collect(toList());
        return new VideoProbeResult(format, codecs);
    }

    public VideoProbeResult.VideoFormat createVideoFormat(FFmpegFormat format) {
        return new VideoProbeResult.VideoFormat(format.duration, format.size, format.bit_rate);
    }

    public VideoProbeResult.Codec createCodec(FFmpegStream stream) {
        return new VideoProbeResult.Codec(stream.codec_name, stream.codec_long_name, stream.bit_rate, stream.codec_type == FFmpegStream.CodecType.AUDIO ? VideoProbeResult.CodecType.AUDIO : VideoProbeResult.CodecType.VIDEO);
    }
}
