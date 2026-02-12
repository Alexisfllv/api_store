package hub.com.api_store.service.impl;

import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.mapper.PurchaseMapper;
import hub.com.api_store.repo.PurchaseRepo;
import hub.com.api_store.service.PurchaseService;
import hub.com.api_store.service.domain.PurchaseServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService {


    private final PurchaseRepo purchaseRepo;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseServiceDomain purchaseServiceDomain;

    // GET
    @Override
    public PurchaseDTOResponse findByPurchaseId(Long id) {
        Purchase purchaseExist = purchaseServiceDomain.findPurchaseById(id);
        PurchaseDTOResponse purchaseDTOResponse = purchaseMapper.toPurchaseDTOResponse(purchaseExist);
        return purchaseDTOResponse;
    }

    @Override
    public PageResponse<PurchaseDTOResponse> findAllListPagePurchase(int page, int size,String prop) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));
        Page<Purchase> purchasePageResponse = purchaseRepo.findAll(pageable);
        return new PageResponse<>(
                purchasePageResponse.getContent()
                        .stream()
                        .map(purchaseMapper::toPurchaseDTOResponse)
                        .toList(),
                purchasePageResponse.getNumber(),
                purchasePageResponse.getSize(),
                purchasePageResponse.getTotalElements(),
                purchasePageResponse.getTotalPages()
        );
    }

}
