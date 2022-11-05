package tcbs.com.cpm.dto.wso2is;

import lombok.Builder;
import lombok.Data;
import tcbs.com.cpm.util.Constants;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class PatchOppRemoveReq implements Serializable {
    private List<String> schemas;
    private List<RemoveOperation> Operations;

    public static PatchOppRemoveReq buildPatchOppRemoveReq(String username, String userId) {
        return PatchOppRemoveReq.builder()
                .schemas(Collections.singletonList(Constants.WSO2_SCHEMAS_PATCHOP))
                .Operations(Collections.singletonList(RemoveOperation.buildRemoveOp(username, userId)))
                .build();
    }
}
