package tcbs.com.cpm.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleReq implements Serializable {
  private Integer id;
  private String name;
  private List<String> system;
  private RoleConfigWSO2Req cfgWso2;
  private RoleConfigBPMReq cfgBpm;
}
