package hub.com.api_store.service;

import hub.com.api_store.dto.purchase.PurchaseDTOResponse;

public interface PurchaseService {

    // GET
    PurchaseDTOResponse findByPurchaseId(Long id);
}
