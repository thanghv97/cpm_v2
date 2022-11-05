package tcbs.com.cpm.dto.wso2is;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import tcbs.com.cpm.util.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class PatchOpAddReq implements Serializable {
    private List<String> schemas;
    @JsonProperty(value = "Operations")
    private List<AddOperation> Operations;

    public static PatchOpAddReq buildPatchOpAddReq(Map<String, String> userMap) {
    return PatchOpAddReq.builder()
            .schemas(Collections.singletonList(Constants.WSO2_SCHEMAS_PATCHOP))
            .Operations(Collections.singletonList(AddOperation.buildAddOp(userMap)))
            .build();
    }
}
