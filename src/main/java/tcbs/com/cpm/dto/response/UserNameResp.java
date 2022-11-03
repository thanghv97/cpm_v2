package tcbs.com.cpm.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserNameResp implements Serializable {
  private int id;
  private String name;
}
