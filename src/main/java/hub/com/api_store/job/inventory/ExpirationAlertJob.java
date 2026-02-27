package hub.com.api_store.job.inventory;

import hub.com.api_store.notification.event.ExpirationAlertEvent;
import hub.com.api_store.repo.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ExpirationAlertJob {
    private final InventoryRepo inventoryRepo;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Clock clock;

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void checkExpiringInventories(){
        LocalDateTime now = LocalDateTime.now(clock);
        // exp 3 days later
        check(now,now.plusDays(3));

        // exp 2 days later
        check(now,now.plusDays(2));

        // exp 1 day later
        check(now,now.plusDays(1));

        // exp
        checkExpired(now);
    }

    private void  check(LocalDateTime start, LocalDateTime end){
        long days = ChronoUnit.DAYS.between(start, end);
        inventoryRepo.findByExpirationDateBetweenOrderByExpirationDateAsc(start, end)
                .forEach(inventory -> applicationEventPublisher.publishEvent(
                        new ExpirationAlertEvent(
                                inventory.getId(),
                                inventory.getProduct().getName(),
                                inventory.getLot(),
                                inventory.getExpirationDate(),
                                days
                        )
                ));
    }

    private void checkExpired(LocalDateTime now){
        inventoryRepo.findByExpirationDateBeforeOrderByExpirationDateAsc(now)
                .forEach(inventory -> applicationEventPublisher.publishEvent(
                        new ExpirationAlertEvent(
                                inventory.getId(),
                                inventory.getProduct().getName(),
                                inventory.getLot(),
                                inventory.getExpirationDate(),
                                ChronoUnit.DAYS.between(now, inventory.getExpirationDate())
                        )
                ));
    }
}
