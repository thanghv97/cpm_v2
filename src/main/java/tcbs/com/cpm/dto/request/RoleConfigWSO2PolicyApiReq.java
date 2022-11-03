package tcbs.com.cpm.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleConfigWSO2PolicyApiReq implements Serializable {
  private String type;
  private String path;
}
