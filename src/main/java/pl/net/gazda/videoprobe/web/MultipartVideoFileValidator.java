package pl.net.gazda.videoprobe.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.ObjectUtils.notEqual;
import static org.springframework.http.MediaType.valueOf;


@Component
public class MultipartVideoFileValidator {
    public static final MediaType VIDEO_TYPE = valueOf("video/*");

    /**
     * @param file to validate against requiredMediaType
     * @param requiredMediaType
     * @throws HttpMediaTypeNotSupportedException if file type doesn't meet requiredMediaType
     */
    public void requiresMediaType(MultipartFile file, MediaType requiredMediaType) throws HttpMediaTypeNotSupportedException {
        MediaType fileMediaType = valueOf(file.getContentType());
        if(mediaTypesNotEqual(fileMediaType, requiredMediaType)) {
            throw new HttpMediaTypeNotSupportedException(fileMediaType, singletonList(VIDEO_TYPE));
        }
    }

    private boolean mediaTypesNotEqual(MediaType fileMediaType, MediaType acceptableMediaType) {
        return notEqual(fileMediaType.getType(), acceptableMediaType.getType());
    }
}
