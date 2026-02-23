package hub.com.api_store.service;

import hub.com.api_store.dto.inventory.InventoryDTOResponse;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.util.page.PageResponse;

import java.util.List;

public interface InventoryService {

    // GET
    InventoryDTOResponse findInventoryById(Long id);
    PageResponse<InventoryDTOResponse> findAllListPageInventory(int page, int size, String prop);
    List<InventoryDTOResponse> findAllListInventoryByProduct(Long productId, int limit);
    List<InventoryDTOResponse> findAllListInventoryByLot(String lot, int limit);
    List<InventoryDTOResponse> findAllListInventoryByWarehouse(String warehouse, int limit);
    // POST
    Inventory addStockFromPurchase(Purchase purchase);

}
