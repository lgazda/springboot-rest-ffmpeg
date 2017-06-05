package pl.net.gazda.videoprobe.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Logs exception and generates exception UUID.
 */
@Component
public class ErrorLogger {
    private static final Logger logger = LoggerFactory.getLogger(ErrorLogger.class);

    public UUID logError(Throwable throwable) {
        UUID uuid = generateUuid();
        logger.error(uuid.toString(), throwable);
        return uuid;
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }
}
