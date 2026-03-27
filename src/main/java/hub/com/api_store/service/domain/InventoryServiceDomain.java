package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Inventory;
import hub.com.api_store.exception.ResourceNotFoundException;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.repo.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryServiceDomain {

    private final InventoryRepo inventoryRepo;

    // findById
    public Inventory findById(Long id){
        return inventoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id
                ));
    }

}
