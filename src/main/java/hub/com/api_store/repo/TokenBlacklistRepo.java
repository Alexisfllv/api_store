package hub.com.api_store.repo;

import hub.com.api_store.entity.security.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepo extends JpaRepository<TokenBlacklist,Long> {

    boolean existsByToken(String token);
}
