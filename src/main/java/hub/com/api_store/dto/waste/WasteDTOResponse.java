package hub.com.api_store.dto.waste;

import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.WasteReason;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WasteDTOResponse(
        Long id,
        BigDecimal quantity,
        GlobalUnit unit,
        WasteReason reason,
        String notes,
        LocalDateTime wasteDate,
        Long inventoryId,
        String inventoryLot,
        String inventoryWarehouse,
        Long productId,
        String productName
) { }
