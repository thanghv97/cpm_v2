package tcbs.com.cpm.error.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ErrorCode implements IErrorCode {
    // 200 - OK
    SUCCESS("200", "Success", HttpStatus.OK),

    // ERROR 400 - BAD_REQUEST
    BAD_REQUEST("400", "Request is not correct", HttpStatus.BAD_REQUEST),
    // ERROR 404 - NOT_FOUND
    NOT_FOUND("404", "No data response", HttpStatus.NOT_FOUND),
    // ERROR 401 - UNAUTHORIZED
    UNAUTHORIZED("401", "You don't have permission for this action", HttpStatus.UNAUTHORIZED),
    // ERROR 403
    FORBIDDEN("403", "You don't have permission for this action", HttpStatus.FORBIDDEN),
    RUNTIME_ERROR("500", "Generic error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    // lookup table to be used to find enum for conversion
    private static final Map<String, ErrorCode> lookup = new HashMap<>();

    static {
        for (ErrorCode e : EnumSet.allOf(ErrorCode.class))
            lookup.put(e.getCode(), e);
    }

    private Integer status;
    private String code;
    private String message;
    private HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }
}
