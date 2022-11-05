package tcbs.com.cpm.dto.response.wso2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class Meta {
  private Date created;
  private Date lastModified;
  private String resourceType;
}
