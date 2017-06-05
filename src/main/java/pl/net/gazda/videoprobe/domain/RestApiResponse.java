package pl.net.gazda.videoprobe.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Represents general rest api response.
 */
@ApiModel(description = "General api response")
@JsonPropertyOrder({"statusCode", "apiCode", "message", "developerMessage", "moreInfo"})
public class RestApiResponse {
    private static final String MESSAGE_NOT_AVAILABLE = "N/A";
    private final String message;
    private final Integer statusCode;
    private final RestApiResultCode apiCode;
    private final String developerMessage;

    private RestApiResponse(String message, HttpStatus statusCode, RestApiResultCode apiCode, String developerMessage) {
        this.message = defaultIfNull(message, MESSAGE_NOT_AVAILABLE);
        this.statusCode = requireNonNull(statusCode).value();
        this.apiCode = defaultIfNull(apiCode, RestApiResultCode.C003);
        this.developerMessage = developerMessage;
    }

    private RestApiResponse(Builder builder) {
        this(builder.message, builder.statusCode, builder.errorCode, builder.developerMessage);
    }

    /**
     * @return user friendly message.
     */

    @ApiParam(value = "message", example = "Missing multipart 'file' parameter")
    public String getMessage() {
        return message;
    }

    /**
     * @return internal api code.
     */
    @ApiParam(example = "C001", required = true)
    public RestApiResultCode getApiCode() {
        return apiCode;
    }

    /**
     * @return http status code.
     */
    @ApiParam(example = "400", required = true)
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @return more info link.
     */
    @ApiParam(example = "More info link")
    public String getMoreInfo() {
        return "http://thecodinglove.com";
    }

    /**
     * @return message for developer / system message - for example exception UUID.
     */
    @ApiParam(example = "ASDSDA32434323432")
    public String getDeveloperMessage() {
        return isNotBlank(developerMessage) ? developerMessage : MESSAGE_NOT_AVAILABLE;
    }

    public static RestApiResponse internalServer(String message, HttpStatus status, RestApiResultCode errorCode, String developerMessage) {
        return new RestApiResponse(message, status, errorCode, developerMessage);
    }

    public static RestApiResponse internalServerError(String message) {
        return builder(INTERNAL_SERVER_ERROR)
                .withErrorCode(RestApiResultCode.C001)
                .withMessage(message)
                .build();
    }

    public static RestApiResponse internalServerError(String message, UUID uuid) {
        return builder(INTERNAL_SERVER_ERROR)
                .withErrorCode(RestApiResultCode.C001)
                .withMessage(message)
                .withDeveloperMessage(uuid.toString())
                .build();
    }

    public static Builder builder(HttpStatus internalServerError) {
        return new Builder(internalServerError);
    }

    public static class Builder {
        private String message;
        private HttpStatus statusCode;
        private RestApiResultCode errorCode;
        private String developerMessage;

        public Builder(HttpStatus status) {
            this.statusCode = status;
        }

        public RestApiResponse build() {
            return new RestApiResponse(this);
        }

        public Builder withMessage(String value) {
            this.message = value;
            return this;
        }

        public Builder withDeveloperMessage(String value) {
            this.developerMessage = value;
            return this;
        }

        public Builder withErrorCode(RestApiResultCode value) {
            this.errorCode = value;
            return this;
        }
    }
}
