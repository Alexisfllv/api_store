package hub.com.api_store.service.domain;

import hub.com.api_store.entity.Purchase;
import hub.com.api_store.nums.ExceptionMessages;
import hub.com.api_store.repo.PurchaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseServiceDomain {

    private final PurchaseRepo purchaseRepo;


    // findById
    public Purchase findPurchaseById(Long id){
        return purchaseRepo.findById(id).orElseThrow(
                () -> new RuntimeException(ExceptionMessages.RESOURCE_NOT_FOUND_ERROR.message()+id));
    }
}
