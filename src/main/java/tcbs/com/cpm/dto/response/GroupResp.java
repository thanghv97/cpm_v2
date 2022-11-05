package tcbs.com.cpm.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class GroupResp implements Serializable {
  private int id;
  private String name;
  private Set<RoleNameResp> roles;
  private Set<UserNameResp> users;
  private String updatedAt;
}
