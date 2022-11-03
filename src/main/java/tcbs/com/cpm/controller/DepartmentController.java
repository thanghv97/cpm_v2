package tcbs.com.cpm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcbs.com.cpm.dto.response.GroupNameResp;
import tcbs.com.cpm.dto.ResponseDTO;
import tcbs.com.cpm.dto.request.DepartmentReq;
import tcbs.com.cpm.dto.response.DepartmentsResp;
import tcbs.com.cpm.entity.Department;
import tcbs.com.cpm.entity.Group;
import tcbs.com.cpm.error.RestApiException;
import tcbs.com.cpm.repository.DepartmentRepository;
import tcbs.com.cpm.repository.GroupRepository;
import tcbs.com.cpm.util.Constants;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("department")
public class DepartmentController {

  @Autowired
  private DepartmentRepository dRepo;

  @Autowired
  private GroupRepository gRepo;

  @PostMapping
  public ResponseEntity<Department> create(@RequestBody DepartmentReq dpReq) {
    Department dp = validate(null, dpReq);
    dp.setName(dpReq.getName());
    dp.setIdParent(dpReq.getIdParent());
    return ResponseEntity.ok(dRepo.save(dp));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Department> update(@PathVariable int id, @RequestBody DepartmentReq dpReq) {
    Department dp = validate(id, dpReq);
    dp.setName(dpReq.getName());
    dp.setIdParent(dpReq.getIdParent());
    return ResponseEntity.ok(dRepo.save(dp));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
    Department dp = validate(id, null);
    dRepo.delete(dp);
    return ResponseEntity.ok(new ResponseDTO(Constants.CODE_DEFAULT, Constants.STATE_SUCCESS, Instant.now()));
  }

  @GetMapping
  public ResponseEntity<DepartmentsResp> gets() {
    // Map id department -> groups
    Map<Integer, List<GroupNameResp>> id2Gs = new HashMap<>();
    List<Group> gs = gRepo.findAll();
    for (Group g : gs) {
      GroupNameResp gn = new GroupNameResp();
      gn.setId(g.getId());
      gn.setName(g.getName());
      id2Gs.computeIfAbsent(g.getDepartmentId(), k -> new ArrayList<>()).add(gn);
    }

    // Map id parent -> departments
    Map<Integer, List<Department>> id2Dps = new HashMap<>();
    List<Department> dps = dRepo.findAll();
    DepartmentsResp root = null;
    for (Department dp : dps) {
      if (dp.getIdParent() == Constants.DEPARTMENT_ROOT_ID) {
        root = new DepartmentsResp();
        root.setId(dp.getId());
        root.setName(dp.getName());
        root.setGroups((id2Gs.get(dp.getId()) == null) ? new ArrayList<>() : id2Gs.get(dp.getId()));
      } else {
        id2Dps.computeIfAbsent(dp.getIdParent(), k -> new ArrayList<>()).add(dp);
      }
    }

    if (root != null) {
      recursive(root, id2Dps, id2Gs);
    }
    return ResponseEntity.ok(root);
  }

  private Department validate(Integer id, DepartmentReq dpReq) {
    if (dpReq != null) {
      if (dpReq.getIdParent() == 0) {
        throw new RestApiException(Constants.CODE_DEFAULT, "Missing id parent when create department !!!");
      }

      Optional<Department> optDp = dRepo.findById(dpReq.getIdParent());
      if (!optDp.isPresent()) {
        throw new RestApiException(Constants.CODE_DEFAULT, "The Department Parent not found!!!");
      }
    }

    // check id is exists
    if (id != null) {
      Optional<Department> optDp = dRepo.findById(id);
      if (!optDp.isPresent()) {
        throw new RestApiException(Constants.CODE_DEFAULT, "The Department not found!!!");
      }
      return optDp.get();
    }
    return new Department();
  }

  private void recursive(DepartmentsResp root, Map<Integer, List<Department>> id2Dps, Map<Integer, List<GroupNameResp>> id2Gs) {
    root.setDepartments(new ArrayList<>());

    if (id2Dps.containsKey(root.getId())) {
      List<Department> dps = id2Dps.get(root.getId());
      for (Department dp : dps) {
        DepartmentsResp child = new DepartmentsResp();
        child.setId(dp.getId());
        child.setName(dp.getName());
        child.setGroups((id2Gs.get(dp.getId()) == null) ? new ArrayList<>() : id2Gs.get(dp.getId()));

        recursive(child, id2Dps, id2Gs);
        root.getDepartments().add(child);
      }
    }
  }
}
