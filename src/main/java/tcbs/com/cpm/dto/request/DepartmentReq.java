package tcbs.com.cpm.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentReq implements Serializable {
  private String name;
  private Integer idParent;
}
