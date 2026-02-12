package hub.com.api_store.service.impl;

import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.mapper.PurchaseMapper;
import hub.com.api_store.repo.PurchaseRepo;
import hub.com.api_store.service.PurchaseService;
import hub.com.api_store.service.domain.PurchaseServiceDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService {


    private final PurchaseRepo purchaseRepo;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseServiceDomain purchaseServiceDomain;

    @Override
    public PurchaseDTOResponse findByPurchaseId(Long id) {
        Purchase purchaseExist = purchaseServiceDomain.findPurchaseById(id);
        PurchaseDTOResponse purchaseDTOResponse = purchaseMapper.toPurchaseDTOResponse(purchaseExist);
        return purchaseDTOResponse;
    }

}
