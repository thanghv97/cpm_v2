package tcbs.com.cpm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ResponseDTO implements Serializable {
  private String code;
  private String message;
  private Instant timestamp;
}
