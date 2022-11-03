package tcbs.com.cpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcbs.com.cpm.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
