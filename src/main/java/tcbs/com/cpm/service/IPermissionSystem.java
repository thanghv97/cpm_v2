package tcbs.com.cpm.service;

import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.entity.User;

import java.util.Set;

public interface IPermissionSystem {
  void addUserToRole(Set<User> users, Role role);
}
