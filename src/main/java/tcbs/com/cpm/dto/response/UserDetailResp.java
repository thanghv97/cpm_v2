package tcbs.com.cpm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tcbs.com.cpm.entity.Role;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResp implements Serializable {
  private int id;
  private String name;
  private Set<RoleResp> roles;
}
