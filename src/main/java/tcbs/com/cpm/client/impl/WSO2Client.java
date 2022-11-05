package tcbs.com.cpm.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tcbs.com.cpm.dto.response.wso2.WSO2GroupInfo;
import tcbs.com.cpm.dto.response.wso2.WSO2UserInfo;
import tcbs.com.cpm.util.Constants;
import tcbs.com.cpm.util.JsonUtils;

import java.util.List;

@Component
public class WSO2Client {
  @Value("${spring.url.wso2is.get-user-info}")
  private String userInfoUrl;

  @Value("${spring.url.wso2is.get-group-info}")
  private String groupInfoUrl;

  @Value("${spring.url.wso2is.token}")
  private String authorizationKey;


  public String getUserInfoWso2(String name){
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(Constants.AUTHORIZATION_HEADER, authorizationKey);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    String url = userInfoUrl + name;
    ResponseEntity<String> wso2UserInfoResponseEntity = new RestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
    String body = wso2UserInfoResponseEntity.getBody();
    JsonObject jsonObject = JsonUtils.parseJsonData2JsonObject(body, null);
    JsonArray resources = JsonUtils.getJsonElement(jsonObject, "Resources", JsonArray.class);
    String id = JsonUtils.getJsonElement((JsonObject) resources.get(0), "id", String.class);
    return id;
  }

  public String getGroupInfoWso2(String groupName){
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(Constants.AUTHORIZATION_HEADER, authorizationKey);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    String url = groupInfoUrl + groupName;
    ResponseEntity<String> wso2GroupInfoResponseEntity = new RestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
    String body = wso2GroupInfoResponseEntity.getBody();
    JsonObject jsonObject = JsonUtils.parseJsonData2JsonObject(body, null);
    JsonArray resources = JsonUtils.getJsonElement(jsonObject, "Resources", JsonArray.class);
    String id = JsonUtils.getJsonElement((JsonObject) resources.get(0), "id", String.class);
    return id;
  }
}
