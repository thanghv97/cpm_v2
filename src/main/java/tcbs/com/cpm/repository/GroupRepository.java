package tcbs.com.cpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tcbs.com.cpm.entity.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
  @Query(value = "SELECT * FROM `group` g " +
    "     INNER JOIN group_user gu on g.id = gu.group_id " +
    "     WHERE gu.user_id = :userId ", nativeQuery = true)
  List<Group> findAllByUserId(int userId);

  List<Group> findAllByDepartmentId(int departmentId);
}
