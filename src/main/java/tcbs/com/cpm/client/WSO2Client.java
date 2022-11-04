package tcbs.com.cpm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcbs.com.cpm.config.FeignClientConfig;
import tcbs.com.cpm.dto.wso2is.PatchOpAddReq;
import tcbs.com.cpm.dto.wso2is.PatchOppRemoveReq;

@FeignClient(name = "wso2is-service", url = "${spring.url.wso2is.scim2}", configuration = FeignClientConfig.class)
public interface WSO2Client {
    @PatchMapping(value = "/Groups/{id}", consumes = "application/json")
    ResponseEntity<Object> addMemberToGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String basicAuthorization,
                                            @PathVariable String id,
                                            @RequestBody PatchOpAddReq body);

    @PostMapping(value = "/Groups/{id}", consumes = "application/json")
    ResponseEntity<Object> removeMemberFromGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String basicAuthorization,
                                                 @PathVariable String id,
                                                 @RequestBody PatchOppRemoveReq body);
}
