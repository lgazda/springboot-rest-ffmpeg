package pl.net.gazda.videoprobe.domain;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Abstraction for storing inputStream resources.
 */
public interface ResourceStore {
    /**
     * @param inputStream
     * @return path to stored resource
     * @throws IOException
     */
    Path store(InputStream inputStream) throws IOException;

    /**
     * Deletes resource with given path.
     * @param path
     * @return true if path was deleted, false if not
     * @throws IOException
     */
    boolean delete(Path path) throws IOException;
}
