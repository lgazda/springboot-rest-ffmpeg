package pl.net.gazda.videoprobe.domain;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.exists;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.nonNull;

/**
 * Simple temporary files implementation of {@link ResourceStore}
 */
@Component
public class TemporaryFileStore implements ResourceStore {
    @Override
    public Path store(InputStream inputStream) throws IOException {
        Path filePath = createTemporaryFile();
        copy(inputStream, filePath, REPLACE_EXISTING);
        return filePath;
    }

    @Override
    public void delete(Path path) throws IOException {
        if(nonNull(path) && exists(path)) {
            Files.delete(path);
        }
    }

    private Path createTemporaryFile() throws IOException {
        Path tempFile = createTempFile("probe-", ".tmp");
        tempFile.toFile().deleteOnExit();
        return tempFile;
    }

}
