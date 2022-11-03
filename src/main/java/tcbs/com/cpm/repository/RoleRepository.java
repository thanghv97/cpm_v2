package tcbs.com.cpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcbs.com.cpm.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
