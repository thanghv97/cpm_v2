package tcbs.com.cpm.error;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tcbs.com.cpm.dto.ResponseDTO;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RestApiException.class)
  public ResponseEntity<ResponseDTO> handlerRestApiException(RestApiException e) {
    ResponseDTO response = new ResponseDTO(e.getCode(), e.getMessage(), Instant.now());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
