package hub.com.api_store.repo;

import hub.com.api_store.entity.Notification;
import hub.com.api_store.nums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    // validate
    boolean existsByInventoryIdAndTypeAndDaysUntilExpiration(
            Long inventoryId,
            NotificationType type,
            Long daysUntilExpiration
    );
}
