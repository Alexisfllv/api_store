package hub.com.api_store.repo;

import hub.com.api_store.entity.Waste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteRepo extends JpaRepository<Waste, Long> {
}
