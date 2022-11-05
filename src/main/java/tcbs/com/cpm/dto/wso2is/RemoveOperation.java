package tcbs.com.cpm.dto.wso2is;

import lombok.Builder;
import lombok.Data;
import tcbs.com.cpm.util.Constants;

import java.io.Serializable;

@Data
@Builder
public class RemoveOperation implements Serializable {
    private String op;
    private String path;

    public static RemoveOperation buildRemoveOp(String username, String userId) {
        String path = String.format("members[display eq \"%s\", value eq \"%s\"]", username, userId);
        return RemoveOperation.builder()
                .op(Constants.WSO2_OP_REMOVE)
                .path(path)
                .build();
    }
}
