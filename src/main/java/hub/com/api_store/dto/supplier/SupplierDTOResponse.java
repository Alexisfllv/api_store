package hub.com.api_store.dto.supplier;

import hub.com.api_store.nums.CategoryStatus;

public record SupplierDTOResponse (
        Long id,
        String name,
        String phone,
        String email,
        String address,
        CategoryStatus status
){}
