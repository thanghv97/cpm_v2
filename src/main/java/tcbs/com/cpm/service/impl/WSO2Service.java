package tcbs.com.cpm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tcbs.com.cpm.client.WSO2Client;
import tcbs.com.cpm.dto.request.RoleConfigWSO2Req;
import tcbs.com.cpm.dto.wso2is.PatchOpAddReq;
import tcbs.com.cpm.dto.wso2is.PatchOppRemoveReq;
import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.entity.User;
import tcbs.com.cpm.service.IPermissionSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class WSO2Service implements IPermissionSystem {

  @Autowired
  private WSO2Client wso2Client;

  @Value("${spring.url.wso2is.token}")
  private String basicAuthorization;

  @Override
  public void addUserToRole(Set<User> users, Role role) {
    // TODO: Get user info from WSO2IS
    Map<String, String> userMap = new HashMap<>();
    // TODO: Get group info from WSO2IS
    String groupId = "qwerty";
    // Add patch user to role
    Object response = wso2Client.addMemberToGroup(basicAuthorization, groupId, PatchOpAddReq.buildPatchOpAddReq(userMap));
    log.info("[WSO2IS][Add] Response: {}", response);
  }

  @Override
  public void deleteUserFromRole(Set<User> users, Role role) {
    // TODO: Get user info from WSO2IS
    Map<String, String> userMap = new HashMap<>();
    // TODO: Get group info from WSO2IS
    String groupId = "qwerty";
    // Remove user from role
    for (Map.Entry<String, String> entry : userMap.entrySet()) {
      Object response = wso2Client.removeMemberFromGroup(basicAuthorization, groupId,
              PatchOppRemoveReq.buildPatchOppRemoveReq(entry.getKey(), entry.getValue()));
      log.info("[WSO2IS][Remove] Response: {}", response);
    }
  }

  public void addRole(String roleName, RoleConfigWSO2Req cfg) {
  }
}
