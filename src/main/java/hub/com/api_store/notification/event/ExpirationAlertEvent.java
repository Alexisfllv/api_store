package hub.com.api_store.notification.event;

import java.time.LocalDateTime;

public record ExpirationAlertEvent(
        Long inventoryId,
        String productName,
        String lot,
        LocalDateTime expirationDate,
        long daysUntilExpiration
) {}