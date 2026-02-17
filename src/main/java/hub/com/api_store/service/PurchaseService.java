package hub.com.api_store.service;

import hub.com.api_store.dto.purchase.PurchaseDTORequest;
import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.util.page.PageResponse;
import org.springframework.data.domain.PageRequest;

public interface PurchaseService {

    // GET
    PurchaseDTOResponse findByPurchaseId(Long id);
    PageResponse<PurchaseDTOResponse> findAllListPagePurchase(int page, int size,String prop);

    // POST
    PurchaseDTOResponse createPurchase(PurchaseDTORequest purchaseDTORequest);
}
