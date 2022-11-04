package tcbs.com.cpm.dto.wso2is;

import lombok.Builder;
import lombok.Data;
import tcbs.com.cpm.util.Constants;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
public class AddOperation implements Serializable {
    private String op;
    private Value value;

    public static AddOperation buildAddOp(Map<String, String> userMap) {
        return AddOperation.builder()
                .op(Constants.WSO2_OP_ADD)
                .value(Value.buildValue(userMap))
                .build();
    }
}
