package tcbs.com.cpm.controller;

import lombok.extern.slf4j.Slf4j;
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
import tcbs.com.cpm.dto.response.RoleNameResp;
import tcbs.com.cpm.dto.response.UserNameResp;
import tcbs.com.cpm.entity.Department;
import tcbs.com.cpm.entity.Group;
import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.entity.User;
import tcbs.com.cpm.error.RestApiException;
import tcbs.com.cpm.repository.DepartmentRepository;
import tcbs.com.cpm.repository.GroupRepository;
import tcbs.com.cpm.repository.RoleRepository;
import tcbs.com.cpm.repository.UserRepository;
import tcbs.com.cpm.service.impl.BPMService;
import tcbs.com.cpm.service.impl.WSO2Service;
import tcbs.com.cpm.util.BeanUtils;
import tcbs.com.cpm.util.Constants;
import tcbs.com.cpm.util.DateTimeUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("group")
@Slf4j
public class GroupController {

  @Autowired
  private GroupRepository gRepo;

  @Autowired
  private DepartmentRepository dRepo;

  @Autowired
  private UserRepository uRepo;

  @Autowired
  private RoleRepository rRepo;

  @Autowired
  private BPMService bpmService;

  @Autowired
  private WSO2Service wso2Service;

  @PostMapping
  public ResponseEntity<Group> create(@RequestBody GroupReq gReq) {
    // validate request
    Group g = validate(null, gReq, true);

    // set properties for group
    BeanUtils.copyPropertiesIgnoreNull(gReq, g);
    if (gReq.getUserIds() != null) {
      g.setUsers((!gReq.getUserIds().isEmpty())
        ? new HashSet<>(uRepo.findAllByIdIn(new ArrayList<>(gReq.getUserIds())))
        : new HashSet<>());
    }
    if (gReq.getRoleIds() != null) {
      g.setRoles((!gReq.getRoleIds().isEmpty())
        ? new HashSet<>(rRepo.findAllByIdIn(new ArrayList<>(gReq.getRoleIds())))
        : new HashSet<>());
      // add users to roles
      addUsersToRole(g.getUsers(), g.getRoles());
    }
    return ResponseEntity.ok(gRepo.save(g));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Group> update(@PathVariable int id, @RequestBody GroupReq gReq) {
    // validate request
    Group gOld = validate(id, gReq, false);

    // set properties for group
    Group gNew = new Group();
    BeanUtils.copyPropertiesIgnoreNull(gOld, gNew);
    BeanUtils.copyPropertiesIgnoreNull(gReq, gNew);
    if (gReq.getUserIds() != null) {
      gNew.setUsers((!gReq.getUserIds().isEmpty())
        ? new HashSet<>(uRepo.findAllByIdIn(new ArrayList<>(gReq.getUserIds())))
        : new HashSet<>());
    }
    if (gReq.getRoleIds() != null) {
      gNew.setRoles((!gReq.getRoleIds().isEmpty())
        ? new HashSet<>(rRepo.findAllByIdIn(new ArrayList<>(gReq.getRoleIds())))
        : new HashSet<>());
    }

    // update roles for user
    updateRolesForUser(gOld, gNew);
    return ResponseEntity.ok(gRepo.save(gNew));
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
      if (g.getRoles() != null) {
        Set<RoleNameResp> rns = new HashSet<>();
        for (Role r : g.getRoles()) {
          rns.add(new RoleNameResp(r.getId(), r.getName()));
        }
        gr.setRoles(rns);
      }
      gr.setUpdatedAt(DateTimeUtils.instantDateToString(Instant.now(), DateTimeUtils.YYYY_MM_DD));
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

  private void updateRolesForUser(Group gOld, Group gNew) {
    // Add/Remove User From Group
    Set<Integer> userIdOld = gOld.getUsers().stream().map(User::getId).collect(Collectors.toSet());
    Set<Integer> userIdNew = gNew.getUsers().stream().map(User::getId).collect(Collectors.toSet());
    Set<User> userRmv = new HashSet<>(gOld.getUsers());
    Set<User> userOld = new HashSet<>(gOld.getUsers());
    Set<User> userNew = new HashSet<>(gNew.getUsers());

    userRmv.removeIf(user -> userIdNew.contains(user.getId()));
    userNew.removeIf(user -> userIdOld.contains(user.getId()));
    userOld.removeIf(user -> !userIdNew.contains(user.getId()));
    addUsersToRole(userNew, gNew.getRoles());
    removeUsersFromRole(userRmv, gOld.getRoles());

    // Add/Remove Role for old Users
    Set<Integer> roleIdOld = gOld.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
    Set<Integer> roleIdNew = gNew.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
    Set<Role> roleRmv = new HashSet<>(gOld.getRoles());
    Set<Role> roleNew = new HashSet<>(gNew.getRoles());

    roleRmv.removeIf(role -> roleIdNew.contains(role.getId()));
    roleNew.removeIf(role -> roleIdOld.contains(role.getId()));

    addUsersToRole(userOld, roleNew);
    removeUsersFromRole(userOld, roleRmv);
  }

  private void addUsersToRole(Set<User> users, Set<Role> roles) {
    if (roles.isEmpty() || users.isEmpty()) {
      return;
    }

    for (Role r : roles) {
      List<String> sys = split(r.getSystem());
      if (sys.isEmpty()) {
        continue;
      }

      for (String s : sys) {
        if (Constants.SYSTEM_BPM.equals(s)) {
          bpmService.addUserToRole(users, r);
        } else if (Constants.SYSTEM_WSO2.equals(s)) {
          wso2Service.addUserToRole(users, r);
        } else {
          log.info("Unsupported system {}", r.getSystem());
        }
      }
    }
  }

  private void removeUsersFromRole(Set<User> users, Set<Role> roles) {
    if (roles.isEmpty() || users.isEmpty()) {
      return;
    }

    for (Role r : roles) {
      List<String> sys = split(r.getSystem());
      if (sys.isEmpty()) {
        continue;
      }

      for (String s : sys) {
        if (Constants.SYSTEM_BPM.equals(s)) {
          bpmService.deleteUserFromRole(users, r);
        } else if (Constants.SYSTEM_WSO2.equals(s)) {
          wso2Service.deleteUserFromRole(users, r);
        } else {
          log.info("Unsupported system {}", r.getSystem());
        }
      }
    }
  }

  private List<String> split(String system) {
    return (system == null) ? Collections.emptyList() : Arrays.asList(system.split(","));
  }
}
