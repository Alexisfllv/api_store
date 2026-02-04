package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Supplier;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.mapper.SupplierMapper;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.repo.SupplierRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupplierServiceDomain {
    private final SupplierRepo supplierRepo;

    // findByIdSupplier
    public Supplier findByIdSupplier(Long id){
        return supplierRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(
                        ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id));
    }
}
