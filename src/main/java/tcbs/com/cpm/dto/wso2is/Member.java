package tcbs.com.cpm.dto.wso2is;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Member implements Serializable {
    private String display;
    private String value;

    public static Member buildMember(String username, String userId) {
        return Member.builder()
                .display(username)
                .value(userId)
                .build();
    }
}
