package tcbs.com.cpm.dto.response.wso2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
@JsonIgnoreProperties
public class WSO2UserInfo {
  private Integer totalResults;
  private Integer startIndex;
  private Integer itemsPerPage;
  private Map<String, List<String>> schemas;
//  private List<Resource> Resources;
}
