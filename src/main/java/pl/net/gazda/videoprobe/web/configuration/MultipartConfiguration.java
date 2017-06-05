package pl.net.gazda.videoprobe.web.configuration;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileCleaningTracker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;
import java.nio.file.Path;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

//@Configuration
//TODO debug / figure our problems with handling multipart requests when using apache commons
public class MultipartConfiguration {
    @Value("${http.multipart.repository.path:null}")
    private String multipartRepositoryPath;
    @Value("${http.multipart.max-file-size}")
    private long multipartMaxFileSize;
    @Value("${http.multipart.max-file-size}")
    private long multipartMaxRequestSize;

    /**
     * This resolver configuration writes every file to disk
     */
    //@Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new TempFileMultipartResolver();
        resolver.setMaxUploadSize(multipartMaxFileSize);
        resolver.setMaxUploadSizePerFile(multipartMaxRequestSize);
        resolver.setMaxInMemorySize(0);
        return resolver;
    }

    /**
     * Disk file item which exposes temp file location.
     */
    public static class DiskTempFileItem extends DiskFileItem {
        public DiskTempFileItem(String fieldName, String contentType, boolean isFormField, String fileName, int sizeThreshold, File repository) {
            super(fieldName, contentType, isFormField, fileName, sizeThreshold, repository);
        }

        @Override
        public File getTempFile() {
            return super.getTempFile();
        }

        public Path getTempFilePath() {
            return getTempFile().toPath();
        }
    }

    public class TempFileMultipartResolver extends CommonsMultipartResolver {
        @Override
        public DiskFileItemFactory getFileItemFactory() {
            return new DiskFileTempItemFactory(0, getRepositoryFile());
        }
    }

    public class DiskFileTempItemFactory extends DiskFileItemFactory {
        public DiskFileTempItemFactory(int sizeThreshold, File repository) {
            super(sizeThreshold, repository);
        }

        @Override
        public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
            DiskTempFileItem result = new DiskTempFileItem(fieldName, contentType, isFormField, fileName, getSizeThreshold(), getRepository());
            FileCleaningTracker tracker = this.getFileCleaningTracker();
            if(tracker != null) {
                tracker.track(result.getTempFile(), result);
            }

            return result;
        }
    }

    private File getRepositoryFile() {
        return isNotBlank(multipartRepositoryPath) ? new File(multipartRepositoryPath) : null;
    }
}
