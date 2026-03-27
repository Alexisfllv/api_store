package hub.com.api_store.dto.inventory;

import hub.com.api_store.nums.GlobalUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryDTOResponse(
   Long id,
   BigDecimal quantity,
   GlobalUnit unit,
   String warehouse,
    String lot,
   LocalDateTime expirationDate,
   Long productId,
    String productName
) {}
