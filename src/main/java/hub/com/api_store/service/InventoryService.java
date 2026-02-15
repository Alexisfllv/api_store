package hub.com.api_store.service;

import hub.com.api_store.entity.Purchase;

public interface InventoryService {
    void addStockFromPurchase(Purchase purchase);
}
