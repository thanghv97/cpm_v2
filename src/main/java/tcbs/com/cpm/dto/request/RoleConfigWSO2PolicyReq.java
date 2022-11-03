package tcbs.com.cpm.dto.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleConfigWSO2PolicyReq implements Serializable {
  private String serviceName;
  private List<RoleConfigWSO2PolicyApiReq> apis;
  private List<String> methods;
}
