package tcbs.com.cpm.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserReq implements Serializable {
  private int id;
  private String name;
}
