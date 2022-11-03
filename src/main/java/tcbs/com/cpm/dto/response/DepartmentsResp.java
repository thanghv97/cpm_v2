package tcbs.com.cpm.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DepartmentsResp implements Serializable {
  private int id;
  private String name;
  private List<DepartmentsResp> departments;
  private List<GroupNameResp> groups;
}
