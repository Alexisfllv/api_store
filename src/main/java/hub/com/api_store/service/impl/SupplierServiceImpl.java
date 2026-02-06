package hub.com.api_store.service.impl;

import hub.com.api_store.dto.supplier.SupplierDTORequest;
import hub.com.api_store.dto.supplier.SupplierDTOResponse;
import hub.com.api_store.dto.supplier.SupplierDTOUpdate;
import hub.com.api_store.entity.Supplier;
import hub.com.api_store.mapper.SupplierMapper;
import hub.com.api_store.nums.CategoryStatus;
import hub.com.api_store.repo.SupplierRepo;
import hub.com.api_store.service.SupplierService;
import hub.com.api_store.service.domain.SupplierServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

    @Override
    public List<SupplierDTOResponse> getListSupplierByName(String name , Integer limit) {
        List<Supplier> supplierList = supplierRepo.findByNameContainingIgnoreCase(name.trim());
        return supplierList
                .stream()
                .map(supplierMapper::toSupplierDTOResponse)
                .limit(limit)
                .toList();
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

    // PUT
    @Override
    public SupplierDTOResponse updateSupplier(Long id, SupplierDTOUpdate supplierDTOUpdate) {
        Supplier supplierExist = supplierServiceDomain.findByIdSupplier(id);

        // If Change or not
        if (!supplierExist.getPhone().equals(supplierDTOUpdate.phone())) {
            supplierServiceDomain.validateExistsByPhone(supplierDTOUpdate.phone());
        }

        if (!supplierExist.getEmail().equals(supplierDTOUpdate.email())) {
            supplierServiceDomain.validateExistsByEmail(supplierDTOUpdate.email());
        }
        supplierExist.setName(supplierDTOUpdate.name());
        supplierExist.setPhone(supplierDTOUpdate.phone());
        supplierExist.setEmail(supplierDTOUpdate.email());
        supplierExist.setAddress(supplierDTOUpdate.address());
        supplierExist.setStatus(supplierDTOUpdate.status());

        Supplier updated = supplierRepo.save(supplierExist);
        SupplierDTOResponse response = supplierMapper.toSupplierDTOResponse(updated);
        return response;
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplierExist = supplierServiceDomain.findByIdSupplier(id);
        supplierExist.setStatus(CategoryStatus.DELETED);
        supplierRepo.save(supplierExist);
    }
}
