package hub.com.api_store.util.response;

public enum StatusApi {
    SUCCESS(200, "Success"),
    CREATED(201, "Created"),
    UPDATED(200, "Updated"),
    DELETED(204, "Deleted"), // <- Mejor práctica REST
    NOT_FOUND(404, "Not Found");

    private final int code;
    private final String message;

    StatusApi(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() { return code; }
    public String message() { return message; }
}