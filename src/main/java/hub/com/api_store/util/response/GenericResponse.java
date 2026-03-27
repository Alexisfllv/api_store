package hub.com.api_store.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenericResponse<T>(
        int status,
        String message,
        T data
) {
    public GenericResponse(StatusApi status, T data) {
        this(status.code(), status.message(), data);
    }

    public GenericResponse(StatusApi status) {
        this(status.code(), status.message(), null);
    }
}