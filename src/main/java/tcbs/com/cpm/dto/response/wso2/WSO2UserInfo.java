package tcbs.com.cpm.dto.response.wso2;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@AllArgsConstructor
@Data
public class WSO2UserInfo {
  private Integer totalResults;
  private Integer startIndex;
  private Integer itemsPerPage;
  private List<String> schemas;
  private List<Resource> Resources;
}
