package hub.com.api_store.dto.notification;

import hub.com.api_store.nums.NotificationType;

import java.time.LocalDateTime;

public record NotificationDTOResponse(
        Long id,
        NotificationType type,
        String message,
        Long inventoryId,
        String productName,
        String lot,
        LocalDateTime expirationDate,
        Long daysUntilExpiration,
        boolean read,
        LocalDateTime sentAt
) {}
