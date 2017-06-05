package pl.net.gazda.videoprobe.service;

import pl.net.gazda.videoprobe.domain.FFMpegVideoProbe;
import pl.net.gazda.videoprobe.domain.FFMpegVideoProbe.VideoProbeException;
import pl.net.gazda.videoprobe.domain.TemporaryFileStore;
import pl.net.gazda.videoprobe.domain.VideoProbeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@Service
public class VideoProbeService {
    private final TemporaryFileStore fileStore;
    private final FFMpegVideoProbe videoProbe;

    @Autowired
    public VideoProbeService(TemporaryFileStore fileStore, FFMpegVideoProbe videoProbe) {
        this.fileStore = fileStore;
        this.videoProbe = videoProbe;
    }

    /**
     * @param inputStream for video file to probe.
     * @return {@link VideoProbeResult}
     * @throws VideoProbeException
     * @throws IOException
     */
    public VideoProbeResult probe(InputStream inputStream) throws VideoProbeException, IOException {
        Path file = null;
        try {
            file = fileStore.store(inputStream);
            return videoProbe.probeVideoFile(file);
        } finally {
            fileStore.delete(file);
        }
    }
}
