package tcbs.com.cpm.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleResp implements Serializable {
  private int id;
  private String name;
  private String system;
  private List<String> groups;
}
