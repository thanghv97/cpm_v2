package tcbs.com.cpm.dto.response;

import lombok.Data;
import tcbs.com.cpm.entity.Role;
import tcbs.com.cpm.entity.User;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
public class GroupResp implements Serializable {
  private String name;
  private Set<Role> roles;
  private Set<User> users;
  private Instant updatedAt;
}
