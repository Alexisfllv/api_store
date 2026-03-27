package hub.com.api_store.dto.category;

import hub.com.api_store.nums.GlobalStatus;

public record CategoryDTOResponse(
        Long id,
        String name,
        String description,
        GlobalStatus status
) {}