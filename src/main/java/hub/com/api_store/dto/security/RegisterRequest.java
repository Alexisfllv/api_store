package hub.com.api_store.dto.security;

import hub.com.api_store.entity.security.Role;

public record RegisterRequest(
        String username,
        String password,
        Role role
) {}
