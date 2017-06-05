package pl.net.gazda.videoprobe.web;

import pl.net.gazda.videoprobe.domain.ErrorLogger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.net.gazda.videoprobe.domain.FFMpegVideoProbe;

import java.util.UUID;

import static java.lang.String.format;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApiResponseEntityFactory responseFactory;
    private final ErrorLogger errorLogger;
    @Value("${spring.http.multipart.max-file-size:N/A}")
    private String maxFileSize;
    @Value("${spring.http.multipart.max-request-size:N/A}")
    private String maxRequestSize;

    @Autowired
    public ControllerExceptionHandler(ApiResponseEntityFactory errorFactory, ErrorLogger errorLogger) {
        this.responseFactory = errorFactory;
        this.errorLogger = errorLogger;
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<Object> handleMaxUploadSizeExceeded(Throwable ex) {
        return responseFactory.badRequest(format("Upload size exceeded - your request was to big. Max request size is %s and for file %s", maxRequestSize, maxFileSize));
    }

    @ExceptionHandler({FFMpegVideoProbe.VideoProbeException.class})
    public ResponseEntity<Object> handleVideoProbeException(FFMpegVideoProbe.VideoProbeException ex) {
        return responseFactory.internal("Problem with probing video file", errorLogger.logError(ex));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        UUID exceptionUUID = errorLogger.logError(ex);
        return isTomcatSizeLimitExceeded(ex) ? handleMaxUploadSizeExceeded(ex) : responseFactory.internal(ex.getMessage(), exceptionUUID);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity<Object> objectResponseEntity = super.handleMissingServletRequestPart(ex, headers, status, request);
        return responseFactory.from(objectResponseEntity, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity<Object> objectResponseEntity = super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
        return responseFactory.from(objectResponseEntity);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity<Object> objectResponseEntity = super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
        return responseFactory.from(objectResponseEntity);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return responseFactory.notFound();
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity<Object> objectResponseEntity = super.handleMissingServletRequestParameter(ex, headers, status, request);
        return responseFactory.from(objectResponseEntity);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseEntity<Object> objectResponseEntity = super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
        return responseFactory.from(objectResponseEntity);
    }

    /**
     * and so on...
     */

    private boolean isTomcatSizeLimitExceeded(Throwable ex) {
        return ex instanceof FileUploadBase.SizeLimitExceededException
                || ExceptionUtils.getRootCause(ex) instanceof FileUploadBase.SizeLimitExceededException;
    }
}
