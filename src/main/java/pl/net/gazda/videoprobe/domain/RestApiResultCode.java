package pl.net.gazda.videoprobe.domain;

/**
 * Represents application/user level api errors.
 */
public enum RestApiResultCode {
    C001("Internal"),
    C002("BadRequest"),
    C003("N/A");

    private final String message;

    RestApiResultCode(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
