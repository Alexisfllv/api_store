package hub.com.api_store.service.impl;

import hub.com.api_store.dto.supplier.SupplierDTORequest;
import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.mapper.SupplierMapper;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.repo.SupplierRepo;
import hub.com.api_store.service.SupplierService;
import hub.com.api_store.service.domain.SupplierServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierMapper supplierMapper;
    private final SupplierServiceDomain supplierServiceDomain;

    // strag
    private final SupplierRepo supplierRepo;


    // GET
    @Override
    public SupplierDTOResponse getSupplierId(Long id) {
        Supplier supplierExist = supplierServiceDomain.findByIdSupplier(id);
        SupplierDTOResponse response = supplierMapper.toSupplierDTOResponse(supplierExist);
        return response;
    }

    @Override
    public PageResponse<SupplierDTOResponse> getPageListSupplier(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,"id"));
        Page<Supplier> pageSupplier = supplierRepo.findAll(pageRequest);

        return new PageResponse<>(
                pageSupplier.getContent()
                        .stream()
                        .map(supplierMapper::toSupplierDTOResponse)
                        .toList(),
                pageSupplier.getNumber(),
                pageSupplier.getSize(),
                pageSupplier.getTotalElements(),
                pageSupplier.getTotalPages()
        );
    }


    // POST
    @Override
    public SupplierDTOResponse addSupplier(SupplierDTORequest supplierDTORequest) {
        Supplier supplier =  supplierMapper.toSupplier(supplierDTORequest);
        supplierServiceDomain.validateExistsByPhone(supplier.getPhone());
        supplierServiceDomain.validateExistsByEmail(supplier.getEmail());
        supplier.setStatus(CategoryStatus.ACTIVE);
        Supplier saved =  supplierRepo.save(supplier);
        SupplierDTOResponse response = supplierMapper.toSupplierDTOResponse(saved);
        return response;
    }
}
