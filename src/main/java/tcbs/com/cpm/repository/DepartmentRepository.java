package tcbs.com.cpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tcbs.com.cpm.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
