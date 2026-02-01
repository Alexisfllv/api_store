package hub.com.api_store.dto.category;

import hub.com.api_store.nums.CategoryStatus;

public record CategoryDTORequest(
        String name,
        String description,
        CategoryStatus status
) {}
