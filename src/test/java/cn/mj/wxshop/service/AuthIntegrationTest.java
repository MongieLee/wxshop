package cn.mj.wxshop.service;

import cn.mj.wxshop.WxshopApplication;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
public class AuthIntegrationTest {
    @Autowired
    private Environment environment;

    private ObjectMapper objectMapper = new ObjectMapper();

    public String requestUrl(String apiName) {
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }

    private static class CustomHttpResponse {
        int code;
        Map<String, Object> body;
        List<Cookie> cookies;

        public CustomHttpResponse(int code, Map<String, Object> body, List<Cookie> cookies) {
            this.code = code;
            this.body = body;
            this.cookies = cookies;
        }

        public int getCode() {
            return code;
        }

        public Map<String, Object> getBody() {
            return body;
        }

        public List<Cookie> getCookies() {
            return cookies;
        }
    }

    private CustomHttpResponse sendHttpRequest(String apiName, String method, Object requestBody, String cookie) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpRequestBase request = null;
        if (HttpMethod.GET.matches(method)) {
            request = new HttpGet(requestUrl(apiName));
        } else if (HttpMethod.POST.matches(method)) {
            request = new HttpPost(requestUrl(apiName));
            if (requestBody != null) {
                String objStr = objectMapper.writeValueAsString(requestBody);
                ((HttpPost) request).setEntity(new StringEntity(objStr));
            }
        }
        if (request != null) {
            request.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            request.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
            if (cookie != null) {
                request.addHeader("Cookie", cookie);
            }
            HttpClientContext context = HttpClientContext.create();
            CloseableHttpResponse response = httpclient.execute(request, context);
            String responseBodyStr = EntityUtils.toString(response.getEntity());
            Map<String, Object> responseBody = objectMapper.readValue(responseBodyStr, Map.class);
            List<Cookie> cookies = context.getCookieStore().getCookies();
            int statusCode = response.getStatusLine().getStatusCode();
            CustomHttpResponse customHttpResponse = new CustomHttpResponse(statusCode, responseBody, cookies);
            return customHttpResponse;
        } else {
            throw new UnsupportedOperationException("not support method");
        }
    }

    @Test
    public void loginLogoutTest() throws IOException {
        // 先进行当前登录状态接口，应返回未登录
        Object isLogin = sendHttpRequest("/api/isLogin", "GET", null, null).getBody().get("isLogin");
        Assertions.assertFalse((Boolean) isLogin);

        // 发送验证码
        int statusCode = sendHttpRequest("/api/code", "POST", TelVerificationServiceTest.VALID_PARAMETER, null).getCode();
        Assertions.assertEquals(HTTP_OK, statusCode);

        // 带着验证码登录，应当返回cookie包含JSESSIONID
        List<Cookie> post = sendHttpRequest("/api/login", "POST", TelVerificationServiceTest.VALID_PARAMETER_CODE, null).getCookies();
        Cookie jsessionid = post.stream().filter(cookie -> cookie.getName().contains("JSESSIONID")).findFirst().get();
        Assertions.assertNotNull(jsessionid);

        // 再次校验当前登录状态，应该为已登录
        isLogin = sendHttpRequest("/api/isLogin", "GET", null, jsessionid.getName() + "=" + jsessionid.getValue()).getBody().get("isLogin");
        Assertions.assertTrue((Boolean) isLogin);

        // 进行注销
        statusCode = sendHttpRequest("/api/logout", "GET", null, jsessionid.getName() + "=" + jsessionid.getValue()).getCode();
        Assertions.assertEquals(HTTP_OK, statusCode);

        // 注销后应该为未登录
        isLogin = sendHttpRequest("/api/isLogin", "GET", null, null).getBody().get("isLogin");
        Assertions.assertFalse((Boolean) isLogin);
    }

    @Test
    public void returnHttpOkWhenParameterIsCorrect() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(requestUrl("/api/code"));
        httpPost.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httpPost.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
        String s = objectMapper.writeValueAsString(TelVerificationServiceTest.VALID_PARAMETER);
        httpPost.setEntity(new StringEntity(s));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        Assertions.assertEquals(HTTP_OK, statusCode);
    }

    @Test
    public void returnHttpBadRequest() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(requestUrl("/api/code"));
        httpPost.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httpPost.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
        String s = objectMapper.writeValueAsString(TelVerificationServiceTest.EMPTY_TEL);
        httpPost.setEntity(new StringEntity(s));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        Assertions.assertEquals(HTTP_BAD_REQUEST, statusCode);
    }
}
