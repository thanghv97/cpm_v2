package tcbs.com.cpm.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tcbs.com.cpm.dto.response.wso2.WSO2GroupInfo;
import tcbs.com.cpm.dto.response.wso2.WSO2UserInfo;
import tcbs.com.cpm.util.Constants;

@Component
public class WSO2Client {
  @Value("${spring.url.wso2.get-user-info}")
  private String userInfoUrl;

  @Value("${spring.url.wso2.get-group-info}")
  private String groupInfoUrl;

  @Value("${spring.keyConfig.authorization}")
  private String authorizationKey;


  public WSO2UserInfo getUserInfoWso2(String name){
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(Constants.AUTHORIZATION_HEADER, authorizationKey);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    String url = userInfoUrl + name;
    ResponseEntity<WSO2UserInfo> wso2UserInfoResponseEntity = new RestTemplate().exchange(url, HttpMethod.GET, requestEntity, WSO2UserInfo.class);
    return wso2UserInfoResponseEntity.getBody();
  }

  public WSO2GroupInfo getGroupInfoWso2(String groupName){
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(Constants.AUTHORIZATION_HEADER, authorizationKey);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    String url = groupInfoUrl + groupName;
    ResponseEntity<WSO2GroupInfo> wso2GroupInfoResponseEntity = new RestTemplate().exchange(url, HttpMethod.GET, requestEntity, WSO2GroupInfo.class);
    return wso2GroupInfoResponseEntity.getBody();
  }
}
