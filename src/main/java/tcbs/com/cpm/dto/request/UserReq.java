package tcbs.com.cpm.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserReq implements Serializable {
  private String name;
}
