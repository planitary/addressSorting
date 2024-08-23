package com.zane.base.handler;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class DoExecutor {
    static String resBody = null;

    public  String doCommonHttpPostForm(Map<String, Object> body, Map<String, String> headers, String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String contentType = "application/x-www-form-urlencoded";
        HttpPost httpPost = new HttpPost(url);
        if (headers.get("Authorization") == null) {
            httpPost.addHeader("Authorization", "Basic YXBwOmFwcA==");
        }
        else {
            httpPost.addHeader("Authorization",headers.get("Authorization"));
        }
        httpPost.addHeader("Content-Type", contentType);
        List<NameValuePair> params = new ArrayList<>();
        log.info("请求体:{},请求头,{},url:{}", body, headers, url);
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
        }
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params);
            httpPost.setEntity(urlEncodedFormEntity);
            HttpResponse response = httpClient.execute(httpPost);
            // 处理响应
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            if (statusCode == 200) {
                resBody = responseBody;
            } else {
                log.debug("请求结果:{}", responseBody);
            }
        } catch (IOException e) {
            log.error("捕获异常{}", e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("捕获异常:{}", e.getMessage());
            }
        }
        return resBody;
    }

    /**
     * 通用请求-json
     * @param body      请求体
     * @param headers   请求头
     * @param url       url
     * @return
     */
    public  String doCommonHttpPostJson(Object body,Map<String,String> headers,String url) {
        String responseJSON = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (headers.get("Authorization") == null) {
            httpPost.addHeader("Authorization", "Basic YXBwOmFwcA==");
        }else {
            httpPost.addHeader("Authorization", headers.get("Authorization"));
        }
        httpPost.addHeader("Content-Type", headers.get("Content-Type"));
        try {
            log.info("json解析源文件:{}", body);
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(body), StandardCharsets.UTF_8);
//            log.info("StringEntity:{}",stringEntity);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String resBody = EntityUtils.toString(httpResponse.getEntity());
            if (statusCode == 200) {
                responseJSON = resBody;
            } else {
                log.info(resBody);
                String errMsg = JSON.parseObject(resBody).getString("error");
                log.debug("请求结果:{}", errMsg);
                throw new RuntimeException("请求失败");
            }
        } catch (IOException e) {
            log.error("捕获异常{}", e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("捕获异常:{}", e.getMessage());
            }
        }
        log.debug("{} ----调用成功",url);
        return responseJSON;
    }
}
