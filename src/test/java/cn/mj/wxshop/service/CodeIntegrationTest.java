package cn.mj.wxshop.service;

import cn.mj.wxshop.WxshopApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
public class CodeIntegrationTest {
    @Autowired
    private Environment environment;

    private ObjectMapper objectMapper = new ObjectMapper();

    public String requestUrl(String apiName) {
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
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
