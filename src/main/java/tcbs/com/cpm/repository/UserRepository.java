package tcbs.com.cpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcbs.com.cpm.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByName(String name);
}
