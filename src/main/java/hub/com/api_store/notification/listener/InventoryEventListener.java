package hub.com.api_store.notification.listener;

import hub.com.api_store.notification.event.ExpirationAlertEvent;
import hub.com.api_store.notification.service.NotificationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventListener {

    private final NotificationEventService notificationService;

    @EventListener
    public void handleExpirationAlert(ExpirationAlertEvent event) {
        notificationService.sendExpirationAlert(event);
    }
}
