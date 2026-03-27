package hub.com.api_store.repo;

import hub.com.api_store.entity.Waste;
import hub.com.api_store.nums.WasteReason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WasteRepo extends JpaRepository<Waste, Long> {
    // findAllByReason
    List<Waste> findByReason(WasteReason reason);

    // findAllByDate
    List<Waste> findByWasteDateBetween(LocalDateTime start, LocalDateTime end);
}
