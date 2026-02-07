package hub.com.api_store.dto.product;

import hub.com.api_store.nums.GlobalStatus;

public record ProductDTOResponse (
            Long id,
            String name,
            String unit,
            GlobalStatus status,
            Long categoryId,
            String categoryName
){}
