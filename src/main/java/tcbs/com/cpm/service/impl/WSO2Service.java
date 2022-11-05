package tcbs.com.cpm.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tcbs.com.cpm.client.IWSO2Client;
import tcbs.com.cpm.client.impl.WSO2Client;
import tcbs.com.cpm.dto.request.RoleConfigWSO2Req;
import tcbs.com.cpm.dto.response.wso2.WSO2GroupInfo;
import tcbs.com.cpm.dto.response.wso2.WSO2UserInfo;
import tcbs.com.cpm.dto.wso2is.PatchOpAddReq;
import tcbs.com.cpm.dto.wso2is.PatchOppRemoveReq;
import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.entity.User;
import tcbs.com.cpm.service.IPermissionSystem;
import tcbs.com.cpm.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class WSO2Service implements IPermissionSystem {

  @Autowired
  private IWSO2Client iWso2Client;

  @Autowired
  private WSO2Client wso2Client;

  @Value("${spring.url.wso2is.token}")
  private String basicAuthorization;

  @Override
  public void addUserToRole(Set<User> users, Role role) {

    // store username-id(resource in wso2 get user info api)
    Map<String, String> userMap = new HashMap<>();

    // Get user info from WSO2IS
    // add userInfo to userMap
    for(User user: users){
      String userId = wso2Client.getUserInfoWso2(user.getName());
      userMap.put(user.getName(), userId);
    }

    // Get group info from WSO2IS
    String groupId = wso2Client.getGroupInfoWso2(role.getName());

    // Add patch user to role
    PatchOpAddReq patchOpAddReq = PatchOpAddReq.buildPatchOpAddReq(userMap);
    Map<String, Object> body = new ObjectMapper().convertValue(patchOpAddReq, Map.class);
    Object response = iWso2Client.addMemberToGroup(basicAuthorization, groupId, body);
    log.info("[WSO2IS][Add] Response: {}", response);
  }

  @Override
  public void deleteUserFromRole(Set<User> users, Role role) {
    // store username-id(resource in wso2 get user info api)
    Map<String, String> userMap = new HashMap<>();

    // Get user info from WSO2IS
    // add userInfo to userMap
    for(User user: users){
      String userId = wso2Client.getUserInfoWso2(user.getName());
      userMap.put(user.getName(), userId);
    }

    // Get group info from WSO2IS
    String groupId = wso2Client.getGroupInfoWso2(role.getName());

    // Remove user from role
    for (Map.Entry<String, String> entry : userMap.entrySet()) {
      PatchOppRemoveReq patchOppRemoveReq = PatchOppRemoveReq.buildPatchOppRemoveReq(entry.getKey(), entry.getValue());
      Map<String, Object> body = new ObjectMapper().convertValue(patchOppRemoveReq, Map.class);
      Object response = iWso2Client.removeMemberFromGroup(basicAuthorization, groupId, body);
      log.info("[WSO2IS][Remove] Response: {}", response);
    }
  }

  public void addRole(String roleName, RoleConfigWSO2Req cfg) {

  }
}
