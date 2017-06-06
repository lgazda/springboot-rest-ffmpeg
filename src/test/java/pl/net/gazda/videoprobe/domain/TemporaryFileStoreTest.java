package pl.net.gazda.videoprobe.domain;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;

public class TemporaryFileStoreTest {
    private static final String TEST_CONTENT = "TEST content";

    private final static TemporaryFileStore testObject = new TemporaryFileStore();

    @Test
    public void should_createAndDeleteFile() throws IOException {
        Path path = testObject.store(new ByteArrayInputStream(TEST_CONTENT.getBytes()));

        assertThat(path).exists();
        assertThat(path).hasContent(TEST_CONTENT);

        boolean delete = testObject.delete(path);

        assertThat(delete).isTrue();
        assertThat(path).doesNotExist();
    }

    @Test
    public void should_doNothing_when_deletingUnExistingPath() throws IOException {
        boolean delete = testObject.delete(Paths.get("fake/path/" + randomNumeric(10)));
        assertThat(delete).isFalse();
    }
}