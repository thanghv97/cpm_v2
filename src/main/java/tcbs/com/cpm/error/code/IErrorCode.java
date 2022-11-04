package tcbs.com.cpm.error.code;

import org.springframework.http.HttpStatus;

public interface IErrorCode {
    Integer getStatus();

    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
