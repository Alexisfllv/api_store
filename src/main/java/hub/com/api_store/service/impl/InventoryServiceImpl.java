package hub.com.api_store.service.impl;

import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.repo.InventoryRepo;
import hub.com.api_store.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {


    private final InventoryRepo inventoryRepo;

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
