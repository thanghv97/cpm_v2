package tcbs.com.cpm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import tcbs.com.cpm.config.FeignClientConfig;

@FeignClient(name = "bpm-service", url = "${spring.url.bpm.base}", configuration = FeignClientConfig.class)
public interface IBPMClient {
  @PostMapping(value = "/group/{groupName}?action=addMember", consumes = "application/json")
  ResponseEntity<Object> addMemberToGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String basicAuthorization,
                                          @PathVariable String groupName,
                                          @RequestParam String user);

  @PostMapping(value = "/group/{groupName}?action=removeMember", consumes = "application/json")
  ResponseEntity<Object> removeMemberFromGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String basicAuthorization,
                                               @PathVariable String groupName,
                                               @RequestParam String user);
}
