package pl.net.gazda.videoprobe.web;

import pl.net.gazda.videoprobe.domain.RestApiResultCode;
import pl.net.gazda.videoprobe.domain.RestApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Component
public class ApiResponseEntityFactory {

    public ResponseEntity<Object> from(ResponseEntity<Object> originalEntity, String message) {
        requireNonNull(originalEntity);

        RestApiResponse error = RestApiResponse.builder(originalEntity.getStatusCode())
                .withMessage(message)
                .withErrorCode(RestApiResultCode.C003)
                .build();

        return ResponseEntity
                .status(originalEntity.getStatusCode())
                .headers(originalEntity.getHeaders())
                .body(error);
    }

    public ResponseEntity<Object> from(ResponseEntity<Object> originalEntity) {
        requireNonNull(originalEntity);

        RestApiResponse error = RestApiResponse.builder(originalEntity.getStatusCode())
                .withMessage(originalEntity.toString())
                .withErrorCode(RestApiResultCode.C003)
                .build();

        return ResponseEntity
                .status(originalEntity.getStatusCode())
                .headers(originalEntity.getHeaders())
                .body(error);
    }

    public ResponseEntity<Object> notFound() {
        RestApiResponse error = RestApiResponse.builder(HttpStatus.NOT_FOUND)
                .withErrorCode(RestApiResultCode.C002)
                .withMessage("Resource not found.")
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    public ResponseEntity<Object> badRequest(String message) {
        RestApiResponse error = RestApiResponse.builder(HttpStatus.BAD_REQUEST)
                .withErrorCode(RestApiResultCode.C002)
                .withMessage(message)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    public ResponseEntity<Object> internal(Throwable ex) {
        requireNonNull(ex);

        RestApiResponse error = RestApiResponse.internalServerError(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    public ResponseEntity<Object> internal(String message, UUID exceptionUUID) {
        requireNonNull(exceptionUUID);

        RestApiResponse error = RestApiResponse.internalServerError(message, exceptionUUID);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

}
