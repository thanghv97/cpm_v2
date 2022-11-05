package tcbs.com.cpm.dto.wso2is;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Value implements Serializable {
    private List<Member> members;

    public static Value buildValue(Map<String, String> userMap) {
        List<Member> memberList = new ArrayList<>();
        if (userMap != null) {
            for (Map.Entry<String, String> entry : userMap.entrySet()) {
                memberList.add(Member.buildMember(entry.getKey(), entry.getValue()));
            }
        }
        return Value.builder()
                .members(memberList)
                .build();
    }
}
