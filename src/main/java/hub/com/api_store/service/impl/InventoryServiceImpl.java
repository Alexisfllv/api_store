package hub.com.api_store.service.impl;

import hub.com.api_store.dto.inventory.InventoryDTOResponse;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Product;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.mapper.InventoryMapper;
import hub.com.api_store.repo.InventoryRepo;
import hub.com.api_store.service.InventoryService;
import hub.com.api_store.service.domain.InventoryServiceDomain;
import hub.com.api_store.service.domain.ProductServiceDomain;
import hub.com.api_store.util.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;

    private final InventoryRepo inventoryRepo;
    private final InventoryServiceDomain inventoryServiceDomain;

    private final ProductServiceDomain productServiceDomain;

    // GET

    @Override
    public InventoryDTOResponse findInventoryById(Long id) {
        Inventory inventory = inventoryServiceDomain.findById(id);
        InventoryDTOResponse inventoryDTOResponse = inventoryMapper.toInventoryDTOResponse(inventory);
        return inventoryDTOResponse;
    }


    @Override
    public PageResponse<InventoryDTOResponse> findAllListPageInventory(int page, int size, String prop) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, prop));
        Page<Inventory> inventoryPage = inventoryRepo.findAll(pageable);
        return new PageResponse<>(
                inventoryPage.getContent()
                        .stream()
                        .map(inventoryMapper::toInventoryDTOResponse)
                        .toList(),
                inventoryPage.getNumber(),
                inventoryPage.getSize(),
                inventoryPage.getTotalElements(),
                inventoryPage.getTotalPages()
        );
    }

    @Override
    public List<InventoryDTOResponse> findAllListInventoryByProduct(Long productId, int limit) {
        productServiceDomain.findById(productId);
        return inventoryRepo.findByProductId(productId)
                .stream()
                .limit(limit)
                .map(inventoryMapper::toInventoryDTOResponse)
                .toList();
    }

    // POST
    @Transactional
    @Override
    public Inventory addStockFromPurchase(Purchase purchase) {

        Optional<Inventory> existing = inventoryRepo
                .findByProductAndLotAndWarehouse(
                        purchase.getProduct(),
                        purchase.getLot(),
                        purchase.getWarehouseLocation()
                );

        if (existing.isPresent()) {
            // Sumar al existente
            Inventory inventory = existing.get();
            inventory.setQuantity(
                    inventory.getQuantity().add(purchase.getQuantity())
            );
            return inventoryRepo.save(inventory);
        } else {
            // Crear nuevo
            Inventory inventory = new Inventory();
            inventory.setProduct(purchase.getProduct());
            inventory.setQuantity(purchase.getQuantity());
            inventory.setUnit(purchase.getUnit());
            inventory.setLot(purchase.getLot());
            inventory.setWarehouse(purchase.getWarehouseLocation());
            inventory.setExpirationDate(purchase.getExpirationDate());
            return inventoryRepo.save(inventory);
        }
    }
}
