package hub.com.api_store.service;

import hub.com.api_store.entity.Inventory;
import hub.com.api_store.entity.Purchase;

public interface InventoryService {
    Inventory addStockFromPurchase(Purchase purchase);
}
