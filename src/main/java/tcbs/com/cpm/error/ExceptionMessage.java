package tcbs.com.cpm.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExceptionMessage {

    public ExceptionMessage(HttpStatus status, String message, String path) {
        this.timestamp = new Date().toString();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
