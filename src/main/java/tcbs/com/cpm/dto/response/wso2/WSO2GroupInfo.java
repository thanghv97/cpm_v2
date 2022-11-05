package tcbs.com.cpm.dto.response.wso2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor

public class WSO2GroupInfo {
  private Integer totalResults;
  private Integer startIndex;
  private Integer itemsPerPage;
  private List<String> schemas;
  private List<Resource> Resources;
}
