package tcbs.com.cpm.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleConfigWSO2Req implements Serializable {
  private RoleConfigWSO2PolicyReq policy;
}
