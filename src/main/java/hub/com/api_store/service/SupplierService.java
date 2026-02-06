package hub.com.api_store.service;

import hub.com.api_store.dto.supplier.SupplierDTORequest;
import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.dto.supplier.SupplierDTOUpdate;
import hub.com.api_store.util.page.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface SupplierService {
    // GET
    SupplierDTOResponse getSupplierId(Long id);
    PageResponse<SupplierDTOResponse> getPageListSupplier(int page, int size);

    // POST
    SupplierDTOResponse addSupplier(SupplierDTORequest supplierDTORequest);

    // PUT
    SupplierDTOResponse updateSupplier(Long id , SupplierDTOUpdate supplierDTOUpdate);

    // DELETE
    void deleteSupplier(Long id);
}
