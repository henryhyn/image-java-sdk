/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qcloud;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

//import org.apache.http.conn.params.ConnRoutePNames;

/**
 * @author jusisli
 */
public class CloudClient {
    protected HttpClient mClient;

    public CloudClient() {
        mClient = new DefaultHttpClient();
        mClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5 * 1000);
    }

    public String post(String url, Map<String, String> header, Map<String, Object> body, byte[] data) throws UnsupportedEncodingException, IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("accept", "*/*");
        httpPost.setHeader("connection", "Keep-Alive");
        httpPost.setHeader("user-agent", "qcloud-java-sdk");
        if (header != null) {
            for (String key : header.keySet()) {
                httpPost.setHeader(key, header.get(key));
            }
        }

        if (false == header.containsKey("Content-Type") || header.get("Content-Type").equals("multipart/form-data")) {
            MultipartEntity multipartEntity = new MultipartEntity();
            if (body != null) {
                for (String key : body.keySet()) {
                    multipartEntity.addPart(key, new StringBody(body.get(key).toString()));
                }
            }

            if (data != null) {
                ContentBody contentBody = new ByteArrayBody(data, "qcloud");
                multipartEntity.addPart("fileContent", contentBody);
            }
            httpPost.setEntity(multipartEntity);
        } else {
            if (data != null) {
                String strBody = new String(data);
                StringEntity stringEntity = new StringEntity(strBody);
                httpPost.setEntity(stringEntity);
            }
        }

//        HttpHost proxy = new HttpHost("127.0.0.1",8888);
//        mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);

        HttpResponse httpResponse = mClient.execute(httpPost);
        int code = httpResponse.getStatusLine().getStatusCode();
        return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
    }

    public String postfiles(String url, Map<String, String> header, Map<String, Object> body, byte[][] data, String[] pornFile) throws UnsupportedEncodingException, IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("accept", "*/*");
        httpPost.setHeader("user-agent", "qcloud-java-sdk");
        if (header != null) {
            for (String key : header.keySet()) {
                httpPost.setHeader(key, header.get(key));
            }
        }

        if (false == header.containsKey("Content-Type") || header.get("Content-Type").equals("multipart/form-data")) {
            MultipartEntity multipartEntity = new MultipartEntity();
            if (body != null) {
                for (String key : body.keySet()) {
                    multipartEntity.addPart(key, new StringBody(body.get(key).toString()));
                }
            }

            if (data != null) {
                for (int i = 0; i < data.length; i++) {
                    ContentBody contentBody = new ByteArrayBody(data[i], pornFile[i]);
                    multipartEntity.addPart("image[" + Integer.toString(i) + "]", contentBody);
                }
            }
            httpPost.setEntity(multipartEntity);
        }

//        HttpHost proxy = new HttpHost("127.0.0.1",8888);
//        mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);

        HttpResponse httpResponse = mClient.execute(httpPost);
        int code = httpResponse.getStatusLine().getStatusCode();
        return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
    }

    public String get(String url, Map<String, String> header, Map<String, String> query) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("accept", "*/*");
        httpGet.setHeader("connection", "Keep-Alive");
        httpGet.setHeader("user-agent", "qcloud-java-sdk");
        httpGet.setHeader("Host", "web.image.myqcloud.com");
        if (header != null) {
            for (String key : header.keySet()) {
                httpGet.setHeader(key, header.get(key));
            }
        }


        if (query != null) {
            String paramStr = "";
            for (String key : query.keySet()) {
                if (!paramStr.isEmpty()) {
                    paramStr += '&';
                }
                paramStr += key + '=' + URLEncoder.encode(query.get(key));
            }

            if (url.indexOf('?') > 0) {
                url += '&' + paramStr;
            } else {
                url += '?' + paramStr;
            }
        }

        HttpResponse httpResponse = mClient.execute(httpGet);
        return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
    }
}
