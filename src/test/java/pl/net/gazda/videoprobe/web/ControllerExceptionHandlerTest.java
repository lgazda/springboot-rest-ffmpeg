package pl.net.gazda.videoprobe.web;


import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.net.gazda.videoprobe.domain.RestApiResultCode;

import java.io.IOException;

import static pl.net.gazda.videoprobe.test.TestUtils.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.properties"})
public class ControllerExceptionHandlerTest {
    @Autowired
    private ControllerExceptionHandler testObject;

    /**
     * More tests here
     */

    @Test
    public void should_returnEntityWithRestApiResponseAndStatus400_when_exceptionIsCausedByTomcatSizeLimitExceeded() {
        ResponseEntity<Object> responseEntity = testObject.handleGlobalException(new RuntimeException(new FileUploadBase.SizeLimitExceededException("size", 10, 5)));
        assertThatResponseHas(responseEntity, HttpStatus.BAD_REQUEST, RestApiResultCode.C002);
    }

    @Test
    public void should_returnEntityWithRestApiResponseAndStatus500_when_exceptionIsNotCausedByTomcatSizeLimitExceeded() {
        ResponseEntity<Object> responseEntity = testObject.handleGlobalException(new RuntimeException());
        ResponseEntity<Object> responseEntity2 = testObject.handleGlobalException(new IOException());

        assertThatResponseHas(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR, RestApiResultCode.C001);
        assertThatResponseHas(responseEntity2, HttpStatus.INTERNAL_SERVER_ERROR, RestApiResultCode.C001);
    }

    private void assertThatResponseHas(ResponseEntity<Object> responseEntity, HttpStatus status, RestApiResultCode code) {
        assertThatResponseEntityHasRestApiBodyAndStatus(responseEntity, status)
                .matches(hasApiResponseCode(code))
                .matches(hasStatusCode(status.value()));
    }

}