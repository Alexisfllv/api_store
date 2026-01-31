package hub.com.api_store.nums;

public enum ExceptionMessages {

    RESOURCE_NOT_FOUND_ERROR("Value dont found: "),
    UNIQUE_EXC("Value already exists: "),
    ;

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}