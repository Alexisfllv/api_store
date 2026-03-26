package hub.com.api_store.repo;

import hub.com.api_store.entity.Waste;
import hub.com.api_store.nums.WasteReason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WasteRepo extends JpaRepository<Waste, Long> {
    // findAllByReason
    List<Waste> findByReason(WasteReason reason);
}
