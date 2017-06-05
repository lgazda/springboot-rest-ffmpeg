package pl.net.gazda.videoprobe.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Final video probe result.
 */
@ApiModel(description = "Video probe result")
@JsonPropertyOrder({ "format", "codecs"})
public class VideoProbeResult {
    private final VideoFormat format;
    private final List<Codec> codecs;

    public VideoProbeResult(VideoFormat format, List<Codec> codecs) {
        this.format = format;
        this.codecs = codecs;
    }

    @JsonProperty("video")
    @ApiModelProperty(value = "Information about video format", required = true)
    public VideoFormat getVideoFormat() {
        return format;
    }

    @JsonProperty("codecs")
    @ApiModelProperty(value = "Information about used audio and video codec", required = true, dataType = "array")
    public List<Codec> getCodecs() {
        return codecs;
    }

    @ApiModel(description="Represent general information about video format")
    public static class VideoFormat {
        private final double duration;
        private final long size;
        private final long bitrate;

        public VideoFormat(double duration, long size, long bitrate) {
            this.duration = duration;
            this.size = size;
            this.bitrate = bitrate;
        }

        @ApiModelProperty(value = "Video duration in seconds", example = "123.45", required = true)
        public double getDuration() {
            return duration;
        }

        @ApiModelProperty(value = "Video size in bytes", example = "1233212", required = true)
        public long getSize() {
            return size;
        }

        @ApiModelProperty(value = "Video bitrate", example = "4124242", required = true)
        public long getBitrate() {
            return bitrate;
        }
    }

    public enum CodecType {
        AUDIO, VIDEO
    }

    @ApiModel(description="Represent general information video/autio codec")
    @JsonPropertyOrder({ "type", "name", "long_name", "bitrate" })
    public static class Codec {
        private final String name;
        private final String longName;
        private final long bitrate;
        private final CodecType type;

        public Codec(String name, String longName, long bitrate, CodecType type) {
            this.name = name;
            this.longName = longName;
            this.bitrate = bitrate;
            this.type = type;
        }

        @ApiModelProperty(value = "Codec name", example = "h264", required = true)
        public String getName() {
            return name;
        }

        @ApiModelProperty(value = "Bitrate", example = "12441", required = true)
        public long getBitrate() {
            return bitrate;
        }

        @ApiModelProperty(value = "Codec type", example = "AUDIO", required = true)
        public CodecType getType() {
            return type;
        }

        @ApiModelProperty(value = "Full codec name", example = "h264", required = true)
        public String getLongName() {
            return longName;
        }
    }
}
