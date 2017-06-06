package pl.net.gazda.videoprobe.domain;

import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.junit.Test;
import pl.net.gazda.videoprobe.domain.VideoProbeResult.Codec;
import pl.net.gazda.videoprobe.domain.VideoProbeResult.CodecType;

import java.util.Collection;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ArrayUtils.isEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.net.gazda.videoprobe.domain.VideoProbeResult.CodecType.AUDIO;
import static pl.net.gazda.videoprobe.domain.VideoProbeResult.CodecType.VIDEO;
import static pl.net.gazda.videoprobe.domain.VideoProbeResultFactory.randomFFmpegProbeResult;

public class VideoProbeResultFactoryTest {
    private static final VideoProbeResultFactory testObject = new VideoProbeResultFactory();

    @Test
    public void should_createEmptyResult_whenFFFormatIsNullAndNoStreams() {
        FFmpegProbeResult fFmpegProbeResult = new FFmpegProbeResult();
        fFmpegProbeResult.streams = emptyList();

        VideoProbeResult result = testObject.from(fFmpegProbeResult);

        assertThat(result).isNotNull();
        assertThat(result.getCodecs()).isEmpty();
        assertThat(result.getVideoFormat()).isNull();
    }

    @Test
    public void should_createFullResult_whenFFmpefProbeResultIsSet() {
        //given
        FFmpegProbeResult fFmpegProbeResult = randomFFmpegProbeResult();
        //when
        VideoProbeResult result = testObject.from(fFmpegProbeResult);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getCodecs()).hasSize(fFmpegProbeResult.getStreams().size());
        assertThat(result.getVideoFormat()).matches(sameAsFFmpegFormat(fFmpegProbeResult.getFormat()));
        assertThat(codecWithType(result.getCodecs(), AUDIO))
                .matches(sameAsFFmpegStream(streamWithType(fFmpegProbeResult.getStreams(), FFmpegStream.CodecType.AUDIO)));
        assertThat(codecWithType(result.getCodecs(), VIDEO))
                .matches(sameAsFFmpegStream(streamWithType(fFmpegProbeResult.getStreams(), FFmpegStream.CodecType.VIDEO)));
    }

    private Codec codecWithType(Collection<Codec> codecs, CodecType type) {
        return codecs.stream().filter(codec -> codec.getType() == type)
                .findAny().orElseThrow(() -> new IllegalArgumentException("No codec with given type: "+ type));
    }

    private FFmpegStream streamWithType(Collection<FFmpegStream> collection, FFmpegStream.CodecType type) {
        return collection.stream().filter(entry -> entry.codec_type == type)
                .findAny().orElseThrow(() -> new IllegalArgumentException("No stream with given type: "+ type));
    }

    private Predicate<Codec> sameAsFFmpegStream(FFmpegStream fFmpegStream) {
        return codec -> codec.getBitrate() == fFmpegStream.bit_rate
                && isEquals(codec.getLongName(), fFmpegStream.codec_long_name)
                && isEquals(codec.getName(), fFmpegStream.codec_name);
    }

    private Predicate<VideoProbeResult.VideoFormat> sameAsFFmpegFormat(FFmpegFormat ffFormat) {
        return videoFormat -> videoFormat.getBitrate() == ffFormat.bit_rate
                && videoFormat.getDuration() == ffFormat.duration
                && videoFormat.getSize() == ffFormat.size ;
    }
}