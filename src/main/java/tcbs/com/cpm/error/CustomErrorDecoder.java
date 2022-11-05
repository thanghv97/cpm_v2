package tcbs.com.cpm.error;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import tcbs.com.cpm.error.code.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public RuntimeException decode(String methodKey, Response response) {
        String message = decodeMessage(response);
        switch (response.status()) {
            case 400:
                return get400Error(methodKey, message);
            case 401:
                return get401Error(methodKey, message);
            case 403:
                return get403Error(message);
            case 404:
                return get404Error(message);
            default:
                return getDefaultError(message);
        }
    }

    private RestApiException get400Error(String methodKey, String message) {
        return new RestApiException(ErrorCode.BAD_REQUEST.getCode(),
                message == null ? ErrorCode.BAD_REQUEST.getMessage() : message);
    }

    private RestApiException get401Error(String methodKey, String message) {
        return new RestApiException(ErrorCode.UNAUTHORIZED.getCode(),
                message == null ? ErrorCode.UNAUTHORIZED.getMessage() : message);
    }

    private RestApiException get403Error(String message) {
        return new RestApiException(ErrorCode.FORBIDDEN.getCode(),
                message == null ? ErrorCode.FORBIDDEN.getMessage() : message);
    }

    private RestApiException get404Error(String message) {
        return new RestApiException(ErrorCode.NOT_FOUND.getCode(),
                message == null ? ErrorCode.NOT_FOUND.getMessage() : message);
    }

    private RestApiException getDefaultError(String message) {
        return new RestApiException(ErrorCode.RUNTIME_ERROR.getCode(),
                message == null ? ErrorCode.RUNTIME_ERROR.getMessage() : message);
    }

    private String decodeMessage(Response response) {
        InputStream inputStream;
        try {
            inputStream = response.body().asInputStream();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            ExceptionMessage exceptionMessage = mapper.readValue(inputStream, ExceptionMessage.class);
            return exceptionMessage.getMessage();
        } catch (IOException e) {
            TypeFactory typeFactory = mapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(List.class, String.class);
            try {
                List<String> errors = mapper.readValue(inputStream, collectionType);
                return errors.toString();
            } catch (IOException e2) {
                log.error(e2.getMessage());
                return null;
            } finally {
                // It is the responsibility of the caller to close the stream.
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException e3) {
                    log.error(e3.getMessage());
                }
            }
        }
    }
}
