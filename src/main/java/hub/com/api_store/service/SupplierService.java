package hub.com.api_store.service;

import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.util.page.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface SupplierService {
    // GET
    SupplierDTOResponse getSupplierId(Long id);
    PageResponse<SupplierDTOResponse> getPageListSupplier(int page, int size);
}
