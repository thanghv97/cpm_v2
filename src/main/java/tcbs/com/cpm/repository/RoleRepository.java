package tcbs.com.cpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcbs.com.cpm.entity.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  List<Role> findAllByIdIn(List<Integer> id);
}
