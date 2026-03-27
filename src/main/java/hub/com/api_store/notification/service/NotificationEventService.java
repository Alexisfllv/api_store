package hub.com.api_store.notification.service;

import hub.com.api_store.entity.Notification;
import hub.com.api_store.notification.event.ExpirationAlertEvent;
import hub.com.api_store.nums.NotificationType;
import hub.com.api_store.repo.NotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationEventService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final NotificationRepo notificationRepo;

    private final Clock clock;


    public void sendExpirationAlert(ExpirationAlertEvent event) {

        // type
        NotificationType type = event.daysUntilExpiration() <= 0
                ? NotificationType.EXPIRED
                : NotificationType.EXPIRING;

        // check duplicates
        if (notificationRepo.existsByInventoryIdAndTypeAndDaysUntilExpiration(
                event.inventoryId(), type, event.daysUntilExpiration())) return;

        // msg
        String message = event.daysUntilExpiration() <= 0
                ? String.format("Lot %s of %s expired %d days ago",
                event.lot(), event.productName(), Math.abs(event.daysUntilExpiration()))
                : String.format("Lot %s of %s expires in %d days",
                event.lot(), event.productName(), event.daysUntilExpiration());


        // save
        Notification notification = new Notification();
        notification.setType(type);
        notification.setMessage(message);
        notification.setInventoryId(event.inventoryId());
        notification.setProductName(event.productName());
        notification.setLot(event.lot());
        notification.setExpirationDate(event.expirationDate());
        notification.setDaysUntilExpiration(event.daysUntilExpiration());
        notification.setRead(false);
        notification.setSentAt(LocalDateTime.now(clock));
        notificationRepo.save(notification);



        // Send wbsocket notification to clients subscribed to the topic
        simpMessagingTemplate.convertAndSend("/topic/expiration-alerts", event);
    }
}
