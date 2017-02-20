package com.ndpmedia.model.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ndpmedia.bean.Fb_docker;
import com.ndpmedia.bean.Gkt_JDBBean;
import com.ndpmedia.bean.Ym_JDBBean;
import com.ndpmedia.comm.config.ConfigUtil;
import com.ndpmedia.comm.url.HttpRequester;
import com.ndpmedia.model.fbDB.dao.FbDBDao;
import com.ndpmedia.model.gktDB.dao.GKTDBDao;
import com.ndpmedia.model.ymDB.dao.YmDBDao;

@Controller
@RequestMapping("/api")
public class Api {
    private static final Logger logger = LoggerFactory.getLogger(Api.class);
    private FbDBDao fbDBDao = new FbDBDao();
    private GKTDBDao gktDBDao = new GKTDBDao();
    private YmDBDao ymDBDao = new YmDBDao();
    
    @ResponseBody
    @RequestMapping(value = "/stopBuild/{build_num}", produces = "text/html;charset=UTF-8")
    public String stopBuild(HttpServletResponse response, @PathVariable(value = "build_num") String build_num){
        //获取jobName buildId
        String jobName = "";
        String buildId = "";
        
        String condition = " where build_num = '"+build_num+"' ";
        if(build_num.indexOf("MY") != -1){
            List<Fb_docker> list = fbDBDao.selectArray(condition);
            if (list.size() > 0){
                Fb_docker bean = list.get(0);
                if (null!=bean) {
                    jobName = bean.getJob_name();
                    buildId = bean.getBuild_id();
                }
            }
        }
        if(build_num.indexOf("GKT") != -1){
            List<Gkt_JDBBean> list = gktDBDao.selectArray(condition); 
            if (list.size() > 0){
                Gkt_JDBBean bean = list.get(0);
                if (null!=bean) {
                    jobName = bean.getJob_name();
                    buildId = bean.getBuild_id();
                }
            }
        }
        if(build_num.indexOf("YM") != -1){
            List<Ym_JDBBean> list = ymDBDao.selectArray(condition);
            if (list.size() > 0){
                Ym_JDBBean bean = list.get(0);
                if (null!=bean) {
                    jobName = bean.getJob_name();
                    buildId = bean.getBuild_id();
                }
            }
        }
        logger.info("build_num:"+build_num+"  jobName:"+jobName+"  buildId:"+buildId);
        String result = "{\"message\":\"jobName or buildId is empty.\"}";
        //停止构建
        if (!jobName.isEmpty() && !buildId.isEmpty()) {
            HttpRequester httpRequester = new HttpRequester();
            String jenkinsUrl = "http://172.30.10.138:8080";
            String stopUrl = jenkinsUrl+"/job/" + jobName + "/" + buildId + "/stop";
            String resultUrl = jenkinsUrl+"/job/" + jobName + "/" + buildId + "/api/json?tree=result";
            
            String message = "";
            int code = 0 ;
            
            try {
                Map <String,String> map = new HashMap<String, String>();
                final String userName = "ivan";
                final String password = "ivan.QWE!@#";
                String nameAndPass = userName + ":" + password;
                String encoding = new String(Base64.encodeBase64(nameAndPass.getBytes()));
                map.put("Authorization","Basic "+encoding);
                logger.info("StopUrl:"+stopUrl);
                code = httpRequester.sendPost(stopUrl,null,map).getCode();
                
                //验证，获取result值
                if (code != 200) {
                    message="Request failed";
                }
                logger.info("resultUrl:"+resultUrl);
                String value = getJenkinsResult(resultUrl);
                logger.info("getJenkinsResult:"+value);
                if (!value.equals("ABORTED")) {
                    message="Stop failure, has been completed. Status is "+value;
                    logger.error("Stop failure, has been completed. Status is "+value);
                }else {
                    message="Has stopped";
                }
                //返回json
                JSONObject jsonBeans = new JSONObject();
                jsonBeans.put("code", code);
                jsonBeans.put("message", message);
                jsonBeans.put("jeninsJobResult", value);
                jsonBeans.put("build_num", build_num);
                jsonBeans.put("jobName", jobName);
                jsonBeans.put("buildId", buildId);
                
                result = jsonBeans.toJSONString();
                logger.info("result:"+result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.addHeader("Access-Control-Allow-Origin", "*");
        return result;
    }
    private String getJenkinsResult(String resultUrl) {
        HttpRequester httpRequester = new HttpRequester();
        String value = "";
        try {
            String content = httpRequester.sendPost(resultUrl).getContent();
            logger.info("获取jenkins结果："+content);
            JSONObject json = JSON.parseObject(content);
            value = json.getString("result");
           
            if (null == value || value.isEmpty()) {
                logger.info("暂停1秒，再次获取jenkins停止结果");
                Thread.sleep(1000);
                value = getJenkinsResult(resultUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
//    public static void main(String[] args) {
//        ConfigUtil.path = "\\main\\resources\\resources\\sql";
//        ConfigUtil configUtil = new ConfigUtil("D:\\workspace\\jenkinsUtil\\src");
//        String build_num ="MY_EC_OFF_714";
//        Api api = new Api();
//        api.getJenkinsResult(build_num);
//    }
}
