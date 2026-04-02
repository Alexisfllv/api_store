package hub.com.api_store.repo;

import hub.com.api_store.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//#3
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
