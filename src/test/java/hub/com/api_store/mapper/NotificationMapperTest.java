package hub.com.api_store.mapper;

import hub.com.api_store.dto.notification.NotificationDTOResponse;
import hub.com.api_store.entity.Notification;
import hub.com.api_store.nums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationMapperTest {

    // mapper pure
    private NotificationMapper notificationMapper;

    @BeforeEach
    void setUp(){
        notificationMapper = new NotificationMapper();
    }

    @Test
    @DisplayName("toNotificationDTOResponse mapped response <- entity")
    void toNotificationDTOResponse(){
        // Arrange
        Notification notification = new Notification(
                1L,
                NotificationType.EXPIRED,
                "message",
                1L,
                "productName",
                "lot",
                LocalDateTime.of(2026, 2, 28, 23, 59, 59),  // ← int no String
                3L,                                          // ← Long
                false,
                LocalDateTime.of(2026, 2, 26, 8, 0, 0)      // ← LocalDateTime
        );

        // Act
        NotificationDTOResponse result = notificationMapper.toNotificationDTOResponse(notification);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(notification.getId(), result.id()),
                () -> assertEquals(notification.getType(), result.type()),
                () -> assertEquals(notification.getMessage(), result.message()),
                () -> assertEquals(notification.getProductName(), result.productName()),
                () -> assertEquals(notification.getLot(), result.lot()),
                () -> assertEquals(notification.getExpirationDate(), result.expirationDate()),
                () -> assertEquals(notification.getDaysUntilExpiration(), result.daysUntilExpiration()),
                () -> assertEquals(notification.isRead(), result.read()),
                () -> assertEquals(notification.getSentAt(), result.sentAt())
        );
    }
}
