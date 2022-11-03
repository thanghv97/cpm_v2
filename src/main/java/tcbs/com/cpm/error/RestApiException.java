package tcbs.com.cpm.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RestApiException extends RuntimeException {
  private final String code;
  private final String message;

  public RestApiException(String code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }
}
