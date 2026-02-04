package hub.com.api_store.service.impl;

import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.mapper.SupplierMapper;
import hub.com.api_store.service.SupplierService;
import hub.com.api_store.service.domain.SupplierServiceDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierMapper supplierMapper;
    private final SupplierServiceDomain supplierServiceDomain;

    @Override
    public SupplierDTOResponse getSupplierId(Long id) {
        Supplier supplierExist = supplierServiceDomain.findByIdSupplier(id);
        SupplierDTOResponse response = supplierMapper.toSupplierDTOResponse(supplierExist);
        return response;
    }
}
