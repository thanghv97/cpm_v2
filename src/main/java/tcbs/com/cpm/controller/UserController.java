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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tcbs.com.cpm.dto.ResponseDTO;
import tcbs.com.cpm.dto.request.UserReq;
import tcbs.com.cpm.dto.response.DepartmentsResp;
import tcbs.com.cpm.dto.response.RoleResp;
import tcbs.com.cpm.dto.response.UserDetailResp;
import tcbs.com.cpm.dto.response.UserNameResp;
import tcbs.com.cpm.dto.response.UserOrgChartResp;
import tcbs.com.cpm.entity.Group;
import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.entity.User;
import tcbs.com.cpm.error.RestApiException;
import tcbs.com.cpm.repository.GroupRepository;
import tcbs.com.cpm.repository.UserRepository;
import tcbs.com.cpm.util.BeanUtils;
import tcbs.com.cpm.util.Constants;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {
  @Autowired
  private UserRepository uRepo;

  @Autowired
  private GroupRepository gRepo;

  @Autowired
  private DepartmentController dController;

  @PostMapping
  public ResponseEntity<UserNameResp> create(@RequestBody UserReq uReq) {
    User u = validate(null, uReq);
    BeanUtils.copyPropertiesIgnoreNull(uReq, u);
    return ResponseEntity.ok(toUserResp(uRepo.save(u)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserNameResp> update(@PathVariable int id, @RequestBody UserReq uReq) {
    User u = validate(id, uReq);
    BeanUtils.copyPropertiesIgnoreNull(uReq, u);
    return ResponseEntity.ok(toUserResp(uRepo.save(u)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseDTO> delete(@PathVariable int id) {
    User u = validate(id, null);
    uRepo.delete(u);
    return ResponseEntity.ok(new ResponseDTO(Constants.CODE_DEFAULT, Constants.STATE_SUCCESS, Instant.now()));
  }

  @DeleteMapping("/from-group")
  public ResponseEntity<UserOrgChartResp> deleteUserFromGroup(@RequestParam int id, @RequestParam int groupId) {
    validate(id, null);
    Optional<Group> optG = gRepo.findById(groupId);
    if (!optG.isPresent()) {
      throw new RestApiException(Constants.CODE_DEFAULT, "Group not found !!!");
    }
    Group g = optG.get();
    g.getUsers().removeIf(user -> user.getId() == id);
    gRepo.save(g);
    return getUserOrgChart(id);
  }

  @GetMapping
  public ResponseEntity<List<UserNameResp>> get() {
    List<User> us = uRepo.findAll();
    List<UserNameResp> resp = new ArrayList<>();
    for (User u : us) {
      resp.add(new UserNameResp(u.getId(), u.getName()));
    }
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/detail/{id}")
  public ResponseEntity<UserDetailResp> getDetail(@PathVariable int id) {
    User u = validate(id, null);
    UserDetailResp resp = new UserDetailResp();
    resp.setId(u.getId());
    resp.setName(u.getName());

    List<Group> gs = gRepo.findAllByUserId(id);
    Map<Integer, RoleResp> roles = new HashMap();
    Set<Integer> roleIds = new HashSet<>();
    if (gs != null) {
      for (Group g : gs) {
        if (!g.getRoles().isEmpty()) {
          for (Role r : g.getRoles()) {
            RoleResp rr = new RoleResp();
            BeanUtils.copyPropertiesIgnoreNull(r, rr);
            if (!roleIds.contains(rr.getId())) {
              rr.setGroups(Collections.singletonList(g.getName()));
              roleIds.add(rr.getId());
              roles.put(rr.getId(), rr);
            } else {
              RoleResp rr_ = roles.get(rr.getId());
              List<String> groups = new ArrayList<>(rr_.getGroups());
              groups.add(g.getName());
              rr_.setGroups(groups);
              roles.put(rr.getId(), rr_);
            }
          }
        }
      }
    }
    resp.setRoles(new HashSet<>(roles.values()));
    return ResponseEntity.ok(resp);
  }

  @GetMapping("/org-chart/{id}")
  public ResponseEntity<UserOrgChartResp> getUserOrgChart(@PathVariable int id) {
    User u = validate(id, null);
    UserOrgChartResp resp = new UserOrgChartResp();
    resp.setId(u.getId());
    resp.setName(u.getName());

    List<Group> gs = gRepo.findAllByUserId(id);
    if (gs != null) {
      boolean hasLength = false;
      Set<Integer> ids = new HashSet<>();
      for (Group g : gs) {
        ids.add(g.getId());
        hasLength = true;
      }
      DepartmentsResp department = dController.gets().getBody();

      if (hasLength && department != null) {
        resp.setDepartment(recursive(department, ids));
      }
    }
    return ResponseEntity.ok(resp);
  }

  private User validate(Integer id, UserReq uReq) {
    // check name is not empty || null
    if (uReq != null) {
      if (uReq.getName() == null || uReq.getName().isEmpty()) {
        throw new RestApiException(Constants.CODE_DEFAULT, "Name of user need to be not empty or not null !!!");
      } else {
        // check duplicate
        Optional<User> optU = uRepo.findByName(uReq.getName());
        if (optU.isPresent()) {
          throw new RestApiException(Constants.CODE_DEFAULT, String.format("User[%s] has existed !!!", uReq.getName()));
        }
      }
    }

    // check id is exists
    if (id != null) {
      Optional<User> optU = uRepo.findById(id);
      if (!optU.isPresent()) {
        throw new RestApiException(Constants.CODE_DEFAULT, String.format("User with 'id'[%d] is not exists !!!", id));
      }
      return optU.get();
    }
    return new User();
  }

  private UserNameResp toUserResp(User u) {
    return new UserNameResp(u.getId(), u.getName());
  }

  private DepartmentsResp recursive(DepartmentsResp root, Set<Integer> ids) {
    boolean hasDepartments = !root.getDepartments().isEmpty();
    boolean hasGroups = !root.getGroups().isEmpty();
    if (!hasGroups && !hasDepartments) {
      return null;
    }

    if (hasDepartments) {
      List<DepartmentsResp> drs = new ArrayList<>();
      for (DepartmentsResp dr : root.getDepartments()) {
        DepartmentsResp drNew = recursive(dr, ids);
        if (drNew != null) {
          drs.add(drNew);
        }
      }
      root.setDepartments(drs);
    }

    if (hasGroups) {
      root.setGroups(root.getGroups().stream().filter(gr -> ids.contains(gr.getId())).collect(Collectors.toList()));
    }

    hasDepartments = !root.getDepartments().isEmpty();
    hasGroups = !root.getGroups().isEmpty();
    return (hasGroups || hasDepartments) ? root : null;
  }
}
