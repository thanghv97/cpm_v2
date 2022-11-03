package tcbs.com.cpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcbs.com.cpm.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}
