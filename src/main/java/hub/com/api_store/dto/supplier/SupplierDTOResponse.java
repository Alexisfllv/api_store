package hub.com.api_store.dto.supplier;

import hub.com.api_store.nums.GlobalStatus;

public record SupplierDTOResponse (
        Long id,
        String name,
        String phone,
        String email,
        String address,
        GlobalStatus status
){}
