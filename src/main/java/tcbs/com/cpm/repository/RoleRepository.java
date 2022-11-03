package tcbs.com.cpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcbs.com.cpm.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
