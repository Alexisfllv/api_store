package hub.com.api_store.notification;

import hub.com.api_store.notification.event.ExpirationAlertEvent;
import hub.com.api_store.notification.listener.InventoryEventListener;
import hub.com.api_store.notification.service.NotificationEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryEventListenerTest {

    @Mock
    private NotificationEventService notificationEventService;

    @InjectMocks
    private InventoryEventListener inventoryEventListener;

    @Test
    @DisplayName("should call sendExpirationAlert when event received")
    void shouldCallSendExpirationAlertWhenEventReceived() {
        // Arrange
        ExpirationAlertEvent event = new ExpirationAlertEvent(
                9L, "Arroz Blanco Premium", "LOT-2026-TEST-001",
                LocalDateTime.of(2026, 2, 28, 23, 59, 59), 2L
        );

        // Act
        inventoryEventListener.handleExpirationAlert(event);

        // Verify
        verify(notificationEventService, times(1)).sendExpirationAlert(event);
        verifyNoMoreInteractions(notificationEventService);
    }
}