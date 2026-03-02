package hub.com.api_store.dto.waste;

import hub.com.api_store.nums.GlobalUnit;
import hub.com.api_store.nums.WasteReason;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WasteDTORequest(
         BigDecimal quantity,
         GlobalUnit unit,
         WasteReason reason,
         String notes,
         Long inventoryId
) {}
