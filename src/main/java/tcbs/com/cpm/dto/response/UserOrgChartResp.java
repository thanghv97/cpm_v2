package tcbs.com.cpm.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserOrgChartResp implements Serializable {
  private int id;
  private String name;
  private DepartmentsResp department;
}
