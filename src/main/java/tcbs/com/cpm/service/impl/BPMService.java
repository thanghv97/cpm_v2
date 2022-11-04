package tcbs.com.cpm.service.impl;

import org.springframework.stereotype.Service;
import tcbs.com.cpm.dto.request.RoleConfigBPMReq;
import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.entity.User;
import tcbs.com.cpm.service.IPermissionSystem;

import java.util.Set;

@Service
public class BPMService implements IPermissionSystem {
  @Override
  public void addUserToRole(Set<User> users, Role role) {

  }

  @Override
  public void deleteUserFromRole(Set<User> users, Role role) {

  }

  public void addRole(String roleName, RoleConfigBPMReq cfg) {
  }

}
