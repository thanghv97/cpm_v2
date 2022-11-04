package tcbs.com.cpm.dto.response.wso2;

import lombok.Data;

import java.util.List;

@Data
public class ResourceGroup {
  private String displayName;
  private Meta meta;
  private List<Member> members;
  private String id;
}
