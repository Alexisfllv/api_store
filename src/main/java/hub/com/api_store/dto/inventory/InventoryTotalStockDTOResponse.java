package hub.com.api_store.dto.inventory;

import hub.com.api_store.nums.GlobalUnit;

import java.math.BigDecimal;

public record InventoryTotalStockDTOResponse(
        Long productId,
        String productName,
        BigDecimal totalQuantity,
        GlobalUnit unit
) {
}
