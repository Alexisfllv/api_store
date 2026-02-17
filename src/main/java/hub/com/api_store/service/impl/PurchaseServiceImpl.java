package hub.com.api_store.service.impl;

import hub.com.api_store.dto.purchase.PurchaseDTORequest;
import hub.com.api_store.dto.purchase.PurchaseDTOResponse;
import hub.com.api_store.dto.purchase.PurchaseDTOUpdate;
import hub.com.api_store.entity.*;
import hub.com.api_store.mapper.PurchaseMapper;
import hub.com.api_store.nums.PurchaseStatus;
import hub.com.api_store.repo.InventoryRepo;
import hub.com.api_store.repo.PurchaseRepo;
import hub.com.api_store.service.InventoryService;
import hub.com.api_store.service.PurchaseService;
import hub.com.api_store.service.domain.ProductServiceDomain;
import hub.com.api_store.service.domain.PurchaseServiceDomain;
import hub.com.api_store.service.domain.SupplierServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseServiceImpl implements PurchaseService {


    private final PurchaseRepo purchaseRepo;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseServiceDomain purchaseServiceDomain;
    private final ProductServiceDomain productServiceDomain;
    private final SupplierServiceDomain supplierServiceDomain;

    // service inventory main
    private final InventoryService inventoryService;
    private final InventoryRepo inventoryRepo;

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

    // POST
    @Transactional
    @Override
    public PurchaseDTOResponse createPurchase(PurchaseDTORequest purchaseDTORequest) {
        Product productExist = productServiceDomain.findById(purchaseDTORequest.productId());
        Supplier supplierExist = supplierServiceDomain.findByIdSupplier(purchaseDTORequest.supplierId());

        Purchase purchaseToSave = purchaseMapper.toPurchase(purchaseDTORequest, productExist, supplierExist);
        // Calculate total cost
        purchaseToSave.setTotalCost(purchaseToSave.getQuantity().multiply(purchaseToSave.getCostUnit()));
        // Set purchase date to current date and time
        purchaseToSave.setPurchaseDate(LocalDateTime.now());
        // Lot
        purchaseToSave.setLot("LOT-"+ LocalDate.now().getYear()+"-"+ purchaseToSave.getLot());
        // invoice number
        purchaseToSave.setInvoiceNumber("INV-"+ LocalDate.now().getYear()+"-"+purchaseToSave.getInvoiceNumber());
        // status
        purchaseToSave.setStatus(PurchaseStatus.RECEIVED);

        // save
        Purchase purchaseSaved = purchaseRepo.save(purchaseToSave);

        Inventory createdInventory = inventoryService.addStockFromPurchase(purchaseSaved);
        purchaseSaved.setInventory(createdInventory);

        // save
        purchaseSaved = purchaseRepo.save(purchaseSaved);

        PurchaseDTOResponse purchaseDTOResponse = purchaseMapper.toPurchaseDTOResponse(purchaseSaved);
        return purchaseDTOResponse;
    }

    @Transactional
    @Override
    public PurchaseDTOResponse updatePurchase(Long id, PurchaseDTOUpdate purchaseDTOUpdate) {
        // Validar existencia de purchase
        Purchase purchaseExist = purchaseServiceDomain.findPurchaseById(id);

        // Validar existencia de product y supplier
        Product productExist = productServiceDomain.findById(purchaseDTOUpdate.productId());
        Supplier supplierExist = supplierServiceDomain.findByIdSupplier(purchaseDTOUpdate.supplierId());

        purchaseExist.setProduct(productExist);
        purchaseExist.setSupplier(supplierExist);
        purchaseExist.setQuantity(purchaseDTOUpdate.quantity());
        purchaseExist.setUnit(purchaseDTOUpdate.unit());
        purchaseExist.setCostUnit(purchaseDTOUpdate.costUnit());
        purchaseExist.setLot("LOT-"+ LocalDate.now().getYear()+"-"+ purchaseDTOUpdate.lot());
        purchaseExist.setExpirationDate(purchaseDTOUpdate.expirationDate());
        purchaseExist.setWarehouseLocation(purchaseDTOUpdate.warehouseLocation());
        purchaseExist.setArrivalDate(purchaseDTOUpdate.arrivalDate());
        purchaseExist.setPurchaseDate(purchaseDTOUpdate.purchaseDate());
        purchaseExist.setInvoiceNumber("INV-"+ LocalDate.now().getYear()+"-"+purchaseDTOUpdate.invoiceNumber());
        purchaseExist.setStatus(purchaseDTOUpdate.purchaseStatus());
        purchaseExist.setNotes(purchaseDTOUpdate.notes());
        purchaseExist.setTotalCost(purchaseDTOUpdate.quantity().multiply(purchaseDTOUpdate.costUnit()));

        // update inventory
        Inventory inventory = purchaseExist.getInventory();
        inventory.setProduct(productExist);
        inventory.setQuantity(purchaseDTOUpdate.quantity());
        inventory.setUnit(purchaseDTOUpdate.unit());
        inventory.setLot(purchaseExist.getLot());
        inventory.setWarehouse(purchaseDTOUpdate.warehouseLocation());
        inventory.setExpirationDate(purchaseDTOUpdate.expirationDate());

        inventoryRepo.save(inventory);
        Purchase update = purchaseRepo.save(purchaseExist);
        PurchaseDTOResponse purchaseDTOResponse = purchaseMapper.toPurchaseDTOResponse(update);
        return purchaseDTOResponse;
    }


}
