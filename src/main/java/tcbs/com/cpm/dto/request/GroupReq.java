package tcbs.com.cpm.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class GroupReq implements Serializable {
  private int departmentId;
  private String name;
  private Set<Long> roleIds;
  private Set<Long> userIds;
}
