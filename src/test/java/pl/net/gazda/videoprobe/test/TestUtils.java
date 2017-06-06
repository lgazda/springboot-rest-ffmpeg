package pl.net.gazda.videoprobe.test;

import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.assertj.core.api.AbstractComparableAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.net.gazda.videoprobe.domain.RestApiResponse;
import pl.net.gazda.videoprobe.domain.RestApiResultCode;
import pl.net.gazda.videoprobe.domain.VideoProbeResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
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

    public static class Assert {
        public static AbstractObjectAssert<?, RestApiResponse> assertThatResponseEntityHasRestApiBodyAndStatus(ResponseEntity<Object> responseEntity, HttpStatus status) {
            assertThatStatusCode(responseEntity).isEqualTo(status);
            assertThatIsInstanceOfRestApiResponse(responseEntity);
            return assertThat((RestApiResponse) responseEntity.getBody());
        }

        public static  AbstractComparableAssert<?, RestApiResultCode> assertThatApiCode(RestApiResponse apiResponse) {
            return assertThat(apiResponse.getApiCode());
        }

        public static AbstractComparableAssert<?, HttpStatus> assertThatStatusCode(ResponseEntity<Object> responseEntity) {
            return assertThat(responseEntity.getStatusCode());
        }

        public static  AbstractComparableAssert<?, Integer> assertThatStatusCode(RestApiResponse restApiResponse) {
            return assertThat(restApiResponse.getStatusCode());
        }

        public static void assertThatIsInstanceOfRestApiResponse(ResponseEntity<Object> responseEntity) {
            assertThat(responseEntity.getBody()).isInstanceOf(RestApiResponse.class);
        }

        public static Predicate<RestApiResponse> hasStatusCode(int value) {
            return restApiResponse -> value == restApiResponse.getStatusCode();
        }

        public static Predicate<RestApiResponse> hasApiResponseCode(RestApiResultCode code) {
            return restApiResponse -> restApiResponse.getApiCode() == code;
        }
    }
}
