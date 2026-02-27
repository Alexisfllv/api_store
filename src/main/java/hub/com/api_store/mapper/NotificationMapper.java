package hub.com.api_store.mapper;


import hub.com.api_store.dto.notification.NotificationDTOResponse;
import hub.com.api_store.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDTOResponse toNotificationDTOResponse(Notification notification) {
        return new NotificationDTOResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getInventoryId(),
                notification.getProductName(),
                notification.getLot(),
                notification.getExpirationDate(),
                notification.getDaysUntilExpiration(),
                notification.isRead(),
                notification.getSentAt()
        );
    }

}
