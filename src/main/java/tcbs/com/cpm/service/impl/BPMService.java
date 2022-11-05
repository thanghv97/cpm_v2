package tcbs.com.cpm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tcbs.com.cpm.client.IBPMClient;
import tcbs.com.cpm.dto.request.RoleConfigBPMReq;
import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.entity.User;
import tcbs.com.cpm.service.IPermissionSystem;

import java.util.Set;

@Service
@Slf4j
public class BPMService implements IPermissionSystem {
  @Autowired
  private IBPMClient ibpmClient;

  @Value("${spring.url.bpm.token}")
  private String basicAuthorization;

  @Override
  public void addUserToRole(Set<User> users, Role role) {
    // Add user to role
    for (User u : users) {
      Object response = ibpmClient.addMemberToGroup(basicAuthorization, role.getName(), u.getName());
      log.info("[BPM][Add] Response: {}", response);
    }
  }

  @Override
  public void deleteUserFromRole(Set<User> users, Role role) {
    // Delete user from role
    for (User u : users) {
      Object response = ibpmClient.removeMemberFromGroup(basicAuthorization, role.getName(), u.getName());
      log.info("[BPM][Remove] Response: {}", response);
    }
  }

  public void addRole(String roleName, RoleConfigBPMReq cfg) {
  }

}
