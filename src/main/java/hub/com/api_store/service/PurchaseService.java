package hub.com.api_store.service;

import hub.com.api_store.dto.purchase.PurchaseDTORequest;
import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.dto.purchase.PurchaseDTOUpdate;
import hub.com.api_store.util.page.PageResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PurchaseService {

    // GET
    PurchaseDTOResponse findByPurchaseId(Long id);
    PageResponse<PurchaseDTOResponse> findAllListPagePurchase(int page, int size,String prop);

    List<PurchaseDTOResponse> findPurchaseListByProductId(Long productId, int limit);
    List<PurchaseDTOResponse> findPurchaseListBySupplierId(Long supplierId, int limit);

    // POST
    PurchaseDTOResponse createPurchase(PurchaseDTORequest purchaseDTORequest);

    // PUT
    PurchaseDTOResponse updatePurchase(Long id, PurchaseDTOUpdate purchaseDTOUpdate);
}
