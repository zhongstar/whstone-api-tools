package com.whstone.utils.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Map;


public class HTTPUtil {


    private static Logger logger = LoggerFactory.getLogger(HTTPUtil.class);

    public static <T, Z> Z post(String url, T data, Class<Z> clazz) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        String s = JSONObject.toJSON(data).toString();

        logger.info("请求参数：" + s);

        StringEntity se = new StringEntity(s, "UTF-8");

        post.setEntity(se);
        post.addHeader(HTTP.CONTENT_TYPE, "application/json");


        //获取response
        CloseableHttpResponse response = httpClient.execute(post);

        //从reponse中解析数据
        //从response中获取json字符串
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity, "UTF-8");
        logger.info("接收参数：" + jsonString);


        return JSONObject.parseObject(jsonString, clazz);
    }

    public static <T, Z> List<Z> postList(String url, T data, Class<Z> clazz) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        String s = JSONObject.toJSON(data).toString();

        logger.info("请求参数：" + s);

        StringEntity se = new StringEntity(s, "UTF-8");

        post.setEntity(se);

        //获取response
        CloseableHttpResponse response = httpClient.execute(post);

        //从reponse中解析数据
        //从response中获取json字符串
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity, "UTF-8");

        logger.info("接收参数：" + jsonString);

        return JSONArray.parseArray(jsonString, clazz);
    }

    public static <T, Z> Z update(String url, T data, Class<Z> clazz) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPatch update = new HttpPatch(url);

        String s = JSONObject.toJSON(data).toString();

        logger.info("请求参数：" + s);

        StringEntity se = new StringEntity(s, "UTF-8");

        update.setEntity(se);

        //获取response
        CloseableHttpResponse response = httpClient.execute(update);

        //从reponse中解析数据
        //从response中获取json字符串
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity, "UTF-8");

        logger.info("接收参数：" + jsonString);

        return JSONObject.parseObject(jsonString, clazz);
    }

    public static <Z> Z get(String url, Class<Z> clazz) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);

        //获取response
        CloseableHttpResponse response = httpClient.execute(get);

        //从reponse中解析数据
        //从response中获取json字符串
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity, "UTF-8");

        logger.info("接收参数：" + jsonString);

        return JSONObject.parseObject(jsonString, clazz);
    }

    public static <Z> Z get(String url, Map<String, String> param, Class<Z> clazz) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        URIBuilder builder = new URIBuilder(url);
        if (param != null) {
            for (String key : param.keySet()) {
                builder.addParameter(key, param.get(key));
            }
        }
        URI uri = builder.build();

        HttpGet get = new HttpGet(uri);

        //获取response
        CloseableHttpResponse response = httpClient.execute(get);

        //从reponse中解析数据
        //从response中获取json字符串
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity, "UTF-8");

        logger.info("接收参数：" + jsonString);

        return JSONObject.parseObject(jsonString, clazz);
    }


    public static <Z> List<Z> getList(String url, Class<Z> clazz) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);

        //获取response
        CloseableHttpResponse response = httpClient.execute(get);

        //从reponse中解析数据
        //从response中获取json字符串
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity, "UTF-8");

        logger.info("接收参数：" + jsonString);

        return JSONArray.parseArray(jsonString, clazz);
    }

    public static <Z> Z delete(String url, Class<Z> clazz) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpDelete delete = new HttpDelete(url);

        //获取response
        CloseableHttpResponse response = httpClient.execute(delete);

        //从reponse中解析数据
        //从response中获取json字符串
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity, "UTF-8");

        logger.info("接收参数：" + jsonString);

        return JSONObject.parseObject(jsonString, clazz);
    }


}
