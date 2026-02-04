package hub.com.api_store.service;

import hub.com.api_store.dto.supplier.SupplierDTOResponse;

public interface SupplierService {
    // GET
    SupplierDTOResponse getSupplierId(Long id);
}
