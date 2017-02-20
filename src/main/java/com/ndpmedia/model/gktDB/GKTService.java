package com.ndpmedia.model.gktDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ndpmedia.bean.Gkt_JDBBean;
import com.ndpmedia.comm.Global;
import com.ndpmedia.comm.config.ConfigUtil;
import com.ndpmedia.comm.url.HttpRequester;
import com.ndpmedia.comm.util.StringUtils;
import com.ndpmedia.comm.vo.CommonResultVO;
import com.ndpmedia.model.gktDB.dao.GKTDBDao;

/**
 * @ClassName: FbDBService
 * @Description: facebook_pmd jenkins db operation
 * @author ivan.wang
 * @date 2015年9月24日 下午4:56:10
 * 
 */
@Controller
@RequestMapping("/gktDB")
public class GKTService {
    private static final Logger logger = LoggerFactory.getLogger(GKTService.class);
    private GKTDBDao gktDBDao = new GKTDBDao();

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public String updateGKTDB(HttpServletRequest request,
            @RequestParam(value = "productTeam", required = false) String productTeam,
            @RequestParam(value = "globalStatus", required = false) String globalStatus,
            @RequestParam(value = "environment", required = false) String environment,
            @RequestParam(value = "sourceCode", required = false) String sourceCode,
            @RequestParam(value = "git_revision", required = false) String git_revision,
            @RequestParam(value = "build_num", required = true) String build_num,
            @RequestParam(value = "build_version", required = false) String build_version,
            @RequestParam(value = "base_build_num", required = false) String base_build_num,
            @RequestParam(value = "base_image_version", required = false) String base_image_version,
            @RequestParam(value = "publish_image_to_aws", required = false) String publish_image_to_aws,
            @RequestParam(value = "concurrent_start", required = false) String concurrent_start,
            @RequestParam(value = "components", required = false) String components,
            @RequestParam(value = "offlineImages", required = false) String offlineImages,
            @RequestParam(value = "onlineImages", required = false) String onlineImages,
            @RequestParam(value = "application_name", required = false) String application_name,
            @RequestParam(value = "log", required = false) String log,
            @RequestParam(value = "end_time", required = false) String end_time,
            @RequestParam(value = "tar_start_time", required = false) String tar_start_time,
            @RequestParam(value = "tar_end_time", required = false) String tar_end_time,
            @RequestParam(value = "tar_complete_flag", required = false) String tar_complete_flag,
            @RequestParam(value = "tar_notbuild_flag", required = false) String tar_notbuild_flag,
            @RequestParam(value = "image_start_time", required = false) String image_start_time,
            @RequestParam(value = "image_end_time", required = false) String image_end_time,
            @RequestParam(value = "image_complete_flag", required = false) String image_complete_flag,
            @RequestParam(value = "deploy_start_time", required = false) String deploy_start_time,
            @RequestParam(value = "deploy_end_time", required = false) String deploy_end_time,
            @RequestParam(value = "deploy_complete_flag", required = false) String deploy_complete_flag,
            @RequestParam(value = "job_name", required = false) String job_name,
            @RequestParam(value = "build_id", required = false) String build_id,
            @RequestParam(value = "creator", required = false) String creator) {

        // 处理MultipartHttpServletRequest请求
        MultipartFile deploy_status_file = null;
        MultipartFile stack_list_file = null;
        MultipartFile resource_list_file = null;
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            deploy_status_file = multipartRequest.getFile("deploy_status");
            stack_list_file = multipartRequest.getFile("stack_list");
            resource_list_file = multipartRequest.getFile("resource_list");
        }
        String deploy_status = null;
        if (deploy_status_file != null && !deploy_status_file.isEmpty()) {
            try {
                byte[] bytes = deploy_status_file.getBytes();
                deploy_status = new String(bytes, "GB2312");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String stack_list = null;
        if (stack_list_file != null && !stack_list_file.isEmpty()) {
            try {
                byte[] bytes = stack_list_file.getBytes();
                stack_list = new String(bytes, "GB2312");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String resource_list = null;
        if (resource_list_file != null && !resource_list_file.isEmpty()) {
            try {
                byte[] bytes = resource_list_file.getBytes();
                resource_list = new String(bytes, "GB2312");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
         * 判断更新还是插入数据
         */
        Gkt_JDBBean gktBean = new Gkt_JDBBean();
        gktBean.beanCreation_time(Global.stringExDatetime(Global.getDateTime()));
        if (end_time != null && !end_time.equals("")) {
            gktBean.beanEnd_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (productTeam != null && !productTeam.equals("")) {
            gktBean.beanProductTeam(productTeam);
        }
        if (globalStatus != null && !globalStatus.equals("")) {
            gktBean.beanGlobalStatus(globalStatus);
        }
        if (environment != null && !environment.equals("")) {
            gktBean.beanEnvironment(environment);
        }
        if (sourceCode != null && !sourceCode.equals("")) {
            gktBean.beanSourceCode(sourceCode);
        }
        if (build_num != null && !build_num.equals("")) {
            gktBean.beanBuild_num(build_num);
        }
        if (build_version != null && !build_version.equals("")) {
            gktBean.beanBuild_version(build_version);
        }
        if (publish_image_to_aws != null && !publish_image_to_aws.equals("")) {
            gktBean.beanPublish_image_to_aws(publish_image_to_aws);
        }
        if (base_build_num != null && !base_build_num.equals("")) {
            gktBean.beanBase_build_num(base_build_num);
        }
        if (base_image_version != null && !base_image_version.equals("")) {
            gktBean.beanBase_image_version(base_image_version);
        }
        if (components != null && !components.equals("")) {
            gktBean.beanComponents(components);
        }
        if (offlineImages != null && !offlineImages.equals("")) {
            gktBean.beanOfflineImages(offlineImages);
        }
        if (onlineImages != null && !onlineImages.equals("")) {
            gktBean.beanOnlineImages(onlineImages);
        }
        if (application_name != null && !application_name.equals("")) {
            gktBean.beanApplication_name(application_name);
        }
        if (creator != null && !creator.equals("")) {
            gktBean.beanCreator(creator);
        }
        if (deploy_status != null && !deploy_status.equals("")) {
            gktBean.beanDeploy_status(deploy_status);
        }
        if (stack_list != null && !stack_list.equals("")) {
            gktBean.beanStack_list(stack_list);
        }
        if (resource_list != null && !resource_list.equals("")) {
            gktBean.beanResource_list(resource_list);
        }
        if (log != null && !log.equals("")) {
            gktBean.beanLog(log);
        }
        if (tar_start_time != null && !tar_start_time.equals("")) {
            gktBean.beanTar_start_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (tar_end_time != null && !tar_end_time.equals("")) {
            gktBean.beanTar_end_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (tar_complete_flag != null && !tar_complete_flag.equals("")) {
            gktBean.beanTar_complete_flag(tar_complete_flag);
        }
        if (tar_notbuild_flag != null && !tar_notbuild_flag.equals("")) {
            gktBean.beanTar_notbuild_flag(tar_notbuild_flag);
        }
        if (image_start_time != null && !image_start_time.equals("")) {
            gktBean.beanImage_start_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (image_end_time != null && !image_end_time.equals("")) {
            gktBean.beanImage_end_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (image_complete_flag != null && !image_complete_flag.equals("")) {
            gktBean.beanImage_complete_flag(image_complete_flag);
        }
        if (deploy_start_time != null && !deploy_start_time.equals("")) {
            gktBean.beanDeploy_start_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (deploy_end_time != null && !deploy_end_time.equals("")) {
            gktBean.beanDeploy_end_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (deploy_complete_flag != null && !deploy_complete_flag.equals("")) {
            gktBean.beanDeploy_complete_flag(deploy_complete_flag);
        }
        if (job_name != null && !job_name.equals("")) {
            gktBean.beanJob_name(job_name);
        }
        if (build_id != null && !build_id.equals("")) {
            gktBean.beanBuild_id(build_id);
        }

        // 获得查询条件components,
        String condition = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("build_num", build_num);
        condition += Global.spellSelectCondition(map);
        // 获得查询总条数
        int sumCount = gktDBDao.selectArray(condition).size();
        int updateResult = 0;
        int saveResult = 0;
        if (sumCount == 1) {
            // 更新数据
            logger.info("ymDB update operation");
            updateResult = gktDBDao.update(gktBean, condition);
        } else {
            // 插入数据
            logger.info("ymDB insert operation");
            saveResult = gktDBDao.save(gktBean);
        }
        String result;
        if (updateResult == 1 || saveResult == 1) {
            logger.info("ymDB operation complete");
            result = "{\"result\":\"success\"}";
        } else {
            logger.error("ymDB operation failure");
            result = "{\"result\":\"failure\"}";
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getGktDB", produces = "application/json;charset=UTF-8")
    public String getGKTDB(HttpServletResponse response, Gkt_JDBBean gktBean,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "pageCapacity", required = false) String pageCapacity,
            @RequestParam(value = "sortName", required = false) String sortName,
            @RequestParam(value = "sortType", required = false) String sortType) {
        String condition = "";
        @SuppressWarnings("unchecked")
        Map<String, String> map = Global.convertBean(gktBean);
        condition += Global.spellSelectCondition(map);
        
        // 获得查询总条数
        int sumCount = gktDBDao.selectArray(condition).size();
        // 排序
        if (sortName != null && !sortName.equals("") && !sortType.equals("")) {
            condition += " order by " + sortName + " " + sortType;
        }
        // 查询分页
        if (page != null & pageCapacity != null) {
            condition += Global.limitPageString(page, pageCapacity);
        }

        List<Gkt_JDBBean> selectArray = gktDBDao.selectArray(condition);
        CommonResultVO<List<Gkt_JDBBean>> commonResultVO = new CommonResultVO<List<Gkt_JDBBean>>();
        commonResultVO.setData(selectArray);
        commonResultVO.setTotal(sumCount);
        String result = StringUtils.toJSON(commonResultVO);

        if (response != null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
        } else {
            logger.warn("response is null");
        }
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/getEnvs", produces = "application/json;charset=UTF-8")
    public String getEnvs() {
        String condition = "GROUP BY environment;";
        List<Gkt_JDBBean> selectArray = gktDBDao.selectArray(condition);
        List<String> envsList = new ArrayList<String>();
        for (Gkt_JDBBean gktBean : selectArray) {
            envsList.add(gktBean.getEnvironment());
        }
        CommonResultVO<List<String>> commonResultVO = new CommonResultVO<List<String>>();
        commonResultVO.setData(envsList);
        commonResultVO.setTotal(envsList.size());
        String result = StringUtils.toJSON(commonResultVO);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getLogInfo", produces = "text/html;charset=UTF-8")
    public String getLogInfo(HttpServletResponse response, @RequestParam(value = "id", required = true) String id) {
        if (id == null || id.equals("")) {
            return "";
        }
        // 获得查询条件components,
        String condition = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        condition += Global.spellSelectCondition(map);
        Gkt_JDBBean gktBean = gktDBDao.getGktBean(condition);

        String jobName = "";
        String build_id = "";
        if (gktBean != null) {
            jobName = gktBean.getJob_name();
            build_id = gktBean.getBuild_id();
        }
        if (jobName == null || jobName.equals("") || build_id == null || build_id.equals("")) {
            return "";
        }
        HttpRequester httpRequester = new HttpRequester();
        String urlString = "http://172.30.10.138:8080/job/" + jobName + "/" + build_id + "/consoleText";
        logger.info(urlString);
        String context = "";
        try {
            context = httpRequester.sendGet(urlString).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.addHeader("Access-Control-Allow-Origin", "*");
        return context;
    }

    public static void main(String[] args) {
        int FCrepose = 85;
        int age = 29;
        double max = (220 - age - FCrepose) * 0.75 + FCrepose;
        double min = (220 - age - FCrepose) * 0.65 + FCrepose;

        System.out.println(min + " " + max);

        ConfigUtil.path = "\\main\\resources\\resources\\sql";
        ConfigUtil configUtil = new ConfigUtil("D:\\workspace\\jenkinsUtil\\src");
        GKTService ymDBService = new GKTService();
        // fbDBService.getLogInfo("792");
        // System.out.println(ymDBService.getSingleFbDB(null, "MY_TON_144"));
        // ymDBService.updateFbDB(null, "YM", "true", null, null, null,
        // "MY_TON_144", null, null, null, null, null, null, null, null, null,
        // null, null, null, null, null, null, null, null, null, null, null,
        // null, null, null);
        // System.out.println(ymDBService.getYMDB(null, null, null, null, null,
        // null, null));
    }
}
