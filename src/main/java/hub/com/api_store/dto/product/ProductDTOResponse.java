package hub.com.api_store.dto.product;

import hub.com.api_store.nums.GlobalStatus;
import hub.com.api_store.nums.GlobalUnit;

public record ProductDTOResponse (
            Long id,
            String name,
            GlobalUnit unit,
            GlobalStatus status,
            Long categoryId,
            String categoryName
){}
