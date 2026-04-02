package hub.com.api_store.dto.security;

public record LoginRequest(
        String username,
        String password
) {}