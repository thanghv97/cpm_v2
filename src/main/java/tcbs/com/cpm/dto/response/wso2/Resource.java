package tcbs.com.cpm.dto.response.wso2;

import lombok.Data;

import java.util.List;

@Data
public class Resource {
  private Meta meta;
  private Name name;
  private List<Group> groups;
  private String id;
  private String userName;
}
