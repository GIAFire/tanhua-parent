package com.itheima.autoconfig.huanxin;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class HuanXinTemplate {

    private HuanXinProperties huanXinProperties;

    private RestTemplate restTemplate;

    public HuanXinTemplate(HuanXinProperties huanXinProperties) {
        this.huanXinProperties = huanXinProperties;
        this.restTemplate = new RestTemplate();
    }

    private String token;

    private long expire = 0L;

    //获取token
    private String getToken() {

        Long now = System.currentTimeMillis();

        if (now > expire) {

            String url = this.huanXinProperties.getUrl()
                    + this.huanXinProperties.getOrgName() + "/"
                    + this.huanXinProperties.getAppName() + "/token";

            Map<String, Object> param = new HashMap<>();

            param.put("grant_type", "client_credentials");
            param.put("client_id", this.huanXinProperties.getClientId());
            param.put("client_secret", this.huanXinProperties.getClientSecret());

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, param, Map.class);
            Map<String, String> responseBody = responseEntity.getBody();
            token = responseBody.get("access_token");
            expire = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        }
        return token;
    }

    /**
     * 注册环信用户
     */
    public void register(String  userId) {

        String url = huanXinProperties.getHuanXinUrl() + "/users";

        String token = getToken();

        //请求头信息
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);

        List<Map> huanXinUsers = new ArrayList<>();
        HashMap<Object, Object> huanxinMap = new HashMap<>();
        huanxinMap.put("username", userId);
        huanxinMap.put("password", "123456");
        huanXinUsers.add(huanxinMap);

        try {
            HttpEntity<String> httpEntity = new HttpEntity(JSON.toJSONString(huanXinUsers), httpHeaders);
            //发起请求
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加好友
     */
    public void addContacts(String userId, String friendId) {

        String targetUrl = huanXinProperties.getHuanXinUrl() + "/users/" + userId + "/contacts/users/" + friendId;

        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json ");
        headers.add("Authorization", "Bearer " + getToken());

        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUrl, httpEntity, String.class);
        } catch (Exception e) {
        }
    }

    /**
     * 发送消息
     *  目标对象id ---陌生人id
     *  消息体msg----消息的json格式
     */
    public void sendMsg(String target, String msg) {

        String targetUrl = huanXinProperties.getHuanXinUrl() + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json ");
        headers.add("Authorization", "Bearer " + getToken());

        try {
            //请求头
            Map<String, Object> requestParam = new HashMap<>();
            requestParam.put("target_type", "users");
            requestParam.put("target", Arrays.asList(target));

            Map<String, Object> msgParam = new HashMap<>();
            msgParam.put("msg", msg);
            msgParam.put("type", "txt");

            requestParam.put("msg", msgParam);

            //表示消息发送者;无此字段Server会默认设置为“from”:“admin”，有from字段但值为空串(“”)时请求失败
            //requestParam.put("from", null);

            HttpEntity<Map> httpEntity = new HttpEntity<>(requestParam, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUrl, httpEntity, String.class);

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }

    /**
     * 解除好友关系
     */
    public void deleteContacts(String userId, String friendId) {
        String targetUrl = huanXinProperties.getHuanXinUrl() + "/users/" + userId + "/contacts/users/" + friendId;
        System.out.println(targetUrl);
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + getToken());

        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            restTemplate.exchange(targetUrl, HttpMethod.DELETE, httpEntity, String.class);
        } catch (Exception e) {

        }
    }
}
