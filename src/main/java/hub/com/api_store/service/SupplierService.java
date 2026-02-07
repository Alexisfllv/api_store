package hub.com.api_store.service;

import hub.com.api_store.dto.supplier.SupplierDTORequest;
import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.dto.supplier.SupplierDTOUpdate;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.util.page.PageResponse;

import java.util.List;

public interface SupplierService {
    // GET
    SupplierDTOResponse getSupplierId(Long id);
    PageResponse<SupplierDTOResponse> getPageListSupplier(int page, int size);
    List<SupplierDTOResponse> getListSupplierByName(String name , Integer limit);
    List<SupplierDTOResponse> getListSupplierByStatus(String status, Integer limit);
    // POST
    SupplierDTOResponse addSupplier(SupplierDTORequest supplierDTORequest);

    // PUT
    SupplierDTOResponse updateSupplier(Long id , SupplierDTOUpdate supplierDTOUpdate);

    // DELETE
    void deleteSupplier(Long id);

    // PATCH
    SupplierDTOResponse changeStatusSupplier(Long id, CategoryStatus status);
}
