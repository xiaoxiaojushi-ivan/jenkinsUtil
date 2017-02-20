package com.ndpmedia.comm;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
* @ClassName: SendUrlRequest 
* @Description: 发送POST请求，并返回请求页面的值
* @author ivan.wang
* @date 2015-8-11 下午4:30:09 
*
 */
public class SendUrlRequest {
    private static final Logger logger = LoggerFactory
            .getLogger(SendUrlRequest.class);

    public static String sendUrlRequest(String urlStr, String username,
            String password) {
        String content_Type = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpEntity entity = null;
        String content = "";
        try {

            // 设置超时时间
            httpclient.getParams().setIntParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
            httpclient.getParams().setParameter(
                    CoreConnectionPNames.SO_TIMEOUT, 20000);

            // 封装需要传递的参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", username));
            nvps.add(new BasicNameValuePair("password", password));
            // 客户端的请求方法类型
            HttpPost httpPost = new HttpPost(urlStr);
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "GBK"));
            HttpResponse response = httpclient.execute(httpPost);

            // 获取服务器返回Http的Content-Type的值
            content_Type = response.getHeaders("Content-Type")[0].getValue()
                    .toString();

            // 获取服务器返回页面的值
            entity = response.getEntity();
            content = EntityUtils.toString(entity);
            logger.info("send_URL:" + urlStr + "?username=" + username
                    + "&password=********");
            logger.info("return_Content_Type:" + content_Type);
            logger.info("return_Content:" + content);
            httpPost.abort();

        } catch (SocketTimeoutException e) {
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return content;
    }
}
