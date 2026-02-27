package hub.com.api_store.dto.inventory;

import java.time.LocalDateTime;

public record ExpirationAlertEvent(
        Long inventoryId,
        String productName,
        String lot,
        LocalDateTime expirationDate,
        long daysUntilExpiration
) {}