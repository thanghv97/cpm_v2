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
import tcbs.com.cpm.dto.ResponseDTO;
import tcbs.com.cpm.dto.request.GroupReq;
import tcbs.com.cpm.dto.response.GroupResp;
import tcbs.com.cpm.dto.response.UserNameResp;
import tcbs.com.cpm.entity.Department;
import tcbs.com.cpm.entity.Group;
import tcbs.com.cpm.entity.User;
import tcbs.com.cpm.error.RestApiException;
import tcbs.com.cpm.repository.DepartmentRepository;
import tcbs.com.cpm.repository.GroupRepository;
import tcbs.com.cpm.repository.UserRepository;
import tcbs.com.cpm.util.BeanUtils;
import tcbs.com.cpm.util.Constants;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("group")
public class GroupController {

  @Autowired
  private GroupRepository gRepo;

  @Autowired
  private DepartmentRepository dRepo;

  @Autowired
  private UserRepository uRepo;

  @PostMapping
  public ResponseEntity<Group> create(@RequestBody GroupReq gReq) {
    Group g = validate(null, gReq, true);
    BeanUtils.copyPropertiesIgnoreNull(gReq, g);
    if (gReq.getUserIds() != null) {
      g.setUsers((!gReq.getUserIds().isEmpty())
        ? new HashSet<>(uRepo.findAllByIdIn(new ArrayList<>(gReq.getUserIds())))
        : new HashSet<>());
    }
    return ResponseEntity.ok(gRepo.save(g));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Group> update(@PathVariable int id, @RequestBody GroupReq gReq) {
    Group g = validate(id, gReq, false);
    BeanUtils.copyPropertiesIgnoreNull(gReq, g);
    if (gReq.getUserIds() != null) {
      g.setUsers((!gReq.getUserIds().isEmpty())
        ? new HashSet<>(uRepo.findAllByIdIn(new ArrayList<>(gReq.getUserIds())))
        : new HashSet<>());
    }
    return ResponseEntity.ok(gRepo.save(g));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
    Group g = validate(id, null, false);
    gRepo.delete(g);
    return ResponseEntity.ok(new ResponseDTO(Constants.CODE_DEFAULT, Constants.STATE_SUCCESS, Instant.now()));
  }

  @GetMapping("/by-department-id/{id}")
  public ResponseEntity<List<GroupResp>> get(@PathVariable int id) {
    List<Group> gs = gRepo.findAllByDepartmentId(id);
    List<GroupResp> gns = new ArrayList<>();
    for (Group g : gs) {
      GroupResp gr = new GroupResp();
      BeanUtils.copyPropertiesIgnoreNull(g, gr);
      if (g.getUsers() != null) {
        Set<UserNameResp> uns = new HashSet<>();
        for (User u : g.getUsers()) {
          uns.add(new UserNameResp(u.getId(), u.getName()));
        }
        gr.setUsers(uns);
      }
      gns.add(gr);
    }
    return ResponseEntity.ok(gns);
  }

  private Group validate(Integer id, GroupReq gReq, boolean isCreate) {
    // check department id is exists
    if (gReq != null) {
      if (isCreate && gReq.getDepartmentId() == null) {
        throw new RestApiException(Constants.CODE_DEFAULT,
          String.format("The Group with 'departmentId'[%d] is not empty or missing when create !!!", gReq.getDepartmentId()));
      }

      if (gReq.getDepartmentId() != null) {
        Optional<Department> optDp = dRepo.findById(gReq.getDepartmentId());
        if (!optDp.isPresent()) {
          throw new RestApiException(Constants.CODE_DEFAULT, String.format("The Department with 'id'[%d] not found!!!", gReq.getDepartmentId()));
        }
      }
    }

    // check id is exists
    if (id != null) {
      Optional<Group> optG = gRepo.findById(id);
      if (!optG.isPresent()) {
        throw new RestApiException(Constants.CODE_DEFAULT, String.format("The Group with 'id'[%d] is not exists !!!", id));
      }
      return optG.get();
    }
    return new Group();
  }
}
