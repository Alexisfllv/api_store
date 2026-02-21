package hub.com.api_store.service;

import hub.com.api_store.dto.inventory.InventoryDTOResponse;
import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Purchase;
import hub.com.api_store.util.page.PageResponse;

public interface InventoryService {

    // GET
    PageResponse<InventoryDTOResponse> findAllListPageInventory(int page, int size, String prop);


    // POST
    Inventory addStockFromPurchase(Purchase purchase);

}
