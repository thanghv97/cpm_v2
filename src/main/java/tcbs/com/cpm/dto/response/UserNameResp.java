package tcbs.com.cpm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserNameResp implements Serializable {
  private int id;
  private String name;
}
