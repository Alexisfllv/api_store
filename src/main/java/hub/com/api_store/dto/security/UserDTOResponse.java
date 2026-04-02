package hub.com.api_store.dto.security;

import hub.com.api_store.entity.security.Role;

public record UserDTOResponse(
        Long id,
        String name,
        String password,
        Role role
) {}
