package com.ndpmedia.model.fbDB;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ndpmedia.bean.Fb_docker;
import com.ndpmedia.bean.Ym_JDBBean;
import com.ndpmedia.comm.Global;
import com.ndpmedia.comm.config.ConfigUtil;
import com.ndpmedia.comm.json.JsonUtil;
import com.ndpmedia.comm.url.HttpRequester;
import com.ndpmedia.comm.util.StringUtils;
import com.ndpmedia.comm.vo.CommonResultVO;
import com.ndpmedia.model.fbDB.dao.FbDBDao;

/**
 * @ClassName: FbDBService
 * @Description: facebook_pmd jenkins db operation
 * @author ivan.wang
 * @date 2015年9月24日 下午4:56:10
 * 
 */
@Controller
@RequestMapping("/fbDB")
public class FbDBService {
    private static final Logger logger = LoggerFactory.getLogger(FbDBService.class);
    private FbDBDao fbDBDao = new FbDBDao();

    @ResponseBody
    @RequestMapping(value = "/update", produces = "text/html;charset=UTF-8")
    public String updateFbDB(HttpServletRequest request,
            @RequestParam(value = "productTeam", required = false) String productTeam,
            @RequestParam(value = "globalStatus", required = false) String globalStatus,
            @RequestParam(value = "env", required = false) String environment,
            @RequestParam(value = "sourceCode", required = false) String sourceCode,
            @RequestParam(value = "git_revision", required = false) String git_revision,
            @RequestParam(value = "build_num", required = true) String build_num,
            @RequestParam(value = "base_build_num", required = false) String base_build_num,
            @RequestParam(value = "base_image_version", required = false) String base_image_version,
            @RequestParam(value = "publish_image_to_aws", required = false) String publish_image_to_aws,
            @RequestParam(value = "concurrent_start", required = false) String concurrent_start,
            @RequestParam(value = "components", required = false) String components,
            @RequestParam(value = "offlineImage", required = false) String offlineImage,
            @RequestParam(value = "onlineImage", required = false) String onlineImage,
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
        Fb_docker fbBean = new Fb_docker();
        fbBean.beanCreation_time(Global.stringExDatetime(Global.getDateTime()));
        if (end_time != null && !end_time.equals("")) {
            fbBean.beanEnd_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (productTeam != null && !productTeam.equals("")) {
            fbBean.beanProductTeam(productTeam);
        }
        if (globalStatus != null && !globalStatus.equals("")) {
            fbBean.beanGlobalStatus(globalStatus);
        }
        if (environment != null && !environment.equals("")) {
            fbBean.beanEnvironment(environment);
        }
        if (sourceCode != null && !sourceCode.equals("")) {
            fbBean.beanSourceCode(sourceCode);
        }
        if (git_revision != null && !git_revision.equals("")) {
            fbBean.beanGit_revision(git_revision);
        }
        if (build_num != null && !build_num.equals("")) {
            fbBean.beanBuild_num(build_num);
        }
        if (base_build_num != null && !base_build_num.equals("")) {
            fbBean.beanBase_build_num(base_build_num);
        }
        if (base_image_version != null && !base_image_version.equals("")) {
            fbBean.beanBase_image_version(base_image_version);
        }
        if (publish_image_to_aws != null && !publish_image_to_aws.equals("")) {
            fbBean.beanPublish_image_to_aws(publish_image_to_aws);
        }
        if (concurrent_start != null && !concurrent_start.equals("")) {
            fbBean.beanConcurrent_start(concurrent_start);
        }
        if (components != null && !components.equals("")) {
            fbBean.beanComponents(components);
        }
        if (offlineImage != null && !offlineImage.equals("")) {
            fbBean.beanOfflineImage(offlineImage);
        }
        if (onlineImage != null && !onlineImage.equals("")) {
            fbBean.beanOnlineImage(onlineImage);
        }
        if (application_name != null && !application_name.equals("")) {
            fbBean.beanApplication_name(application_name);
        }
        if (creator != null && !creator.equals("")) {
            fbBean.beanCreator(creator);
        }
        if (deploy_status != null && !deploy_status.equals("")) {
            fbBean.beanDeploy_status(deploy_status);
        }
        if (stack_list != null && !stack_list.equals("")) {
            fbBean.beanStack_list(stack_list);
        }
        if (resource_list != null && !resource_list.equals("")) {
            fbBean.beanResource_list(resource_list);
        }
        if (log != null && !log.equals("")) {
            fbBean.beanLog(log);
        }
        if (tar_start_time != null && !tar_start_time.equals("")) {
            fbBean.beanTar_start_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (tar_end_time != null && !tar_end_time.equals("")) {
            fbBean.beanTar_end_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (tar_complete_flag != null && !tar_complete_flag.equals("")) {
            fbBean.beanTar_complete_flag(tar_complete_flag);
        }
        if (tar_notbuild_flag != null && !tar_notbuild_flag.equals("")) {
            fbBean.beanTar_notbuild_flag(tar_notbuild_flag);
        }
        if (image_start_time != null && !image_start_time.equals("")) {
            fbBean.beanImage_start_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (image_end_time != null && !image_end_time.equals("")) {
            fbBean.beanImage_end_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (image_complete_flag != null && !image_complete_flag.equals("")) {
            fbBean.beanImage_complete_flag(image_complete_flag);
        }
        if (deploy_start_time != null && !deploy_start_time.equals("")) {
            fbBean.beanDeploy_start_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (deploy_end_time != null && !deploy_end_time.equals("")) {
            fbBean.beanDeploy_end_time(Global.stringExDatetime(Global.getDateTime()));
        }
        if (deploy_complete_flag != null && !deploy_complete_flag.equals("")) {
            fbBean.beanDeploy_complete_flag(deploy_complete_flag);
        }
        if (job_name != null && !job_name.equals("")) {
            fbBean.beanJob_name(job_name);
        }
        if (build_id != null && !build_id.equals("")) {
            fbBean.beanBuild_id(build_id);
        }

        // 获得查询条件components,
        String condition = "";
        Map<String, String> map = new HashMap<String, String>();
        if (environment != null && !environment.equals("")) {
            map.put("environment", environment);
        }
        map.put("build_num", build_num);
        condition += Global.spellSelectCondition(map);
        // 获得查询总条数
        int sumCount = fbDBDao.selectArray(condition).size();
        int updateResult = 0;
        int saveResult = 0;
        if (sumCount == 1) {
            // 更新数据
            logger.info("fbDB update operation");
            updateResult = fbDBDao.update(fbBean, condition);
        } else {
            // 插入数据
            logger.info("fbDB insert operation");
            saveResult = fbDBDao.save(fbBean);
        }
        String result;
        if (updateResult == 1 || saveResult == 1) {
            logger.info("fbDB operation complete");
            result = "{\"result\":\"success\"}";
        } else {
            logger.error("fbDB operation failure");
            result = "{\"result\":\"failure\"}";
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getFbDB", produces = "text/html;charset=UTF-8")
    public String getFbDB(HttpServletResponse response, @RequestParam("page") String page,
            @RequestParam("pageCapacity") String pageCapacity, @RequestParam("creator") String creator,
            @RequestParam("env") String environment, @RequestParam("sortName") String sortName,
            @RequestParam("sortType") String sortType) throws UnsupportedEncodingException {
        String condition = "";
        // 查询条件
        Map<String, String> map = new HashMap<String, String>();
        map.put("creator", creator);
        map.put("environment", environment);
        condition += Global.spellSelectCondition(map);
        // 获得查询总条数
        int sumCount = fbDBDao.selectArray(condition).size();
        logger.info("sumCount:" + sumCount);
        // 排序
        if (!sortName.equals("") && !sortType.equals("")) {
            condition += " order by " + sortName + " " + sortType;
        }
        // 查询分页
        condition += Global.limitPageString(page, pageCapacity);

        String result = JsonUtil.beansToJson(fbDBDao.selectArray(condition), sumCount, "heihei");

        response.addHeader("Access-Control-Allow-Origin", "*");
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/getEnvs", produces = "application/json;charset=UTF-8")
    public String getEnvs() {
        String condition = "GROUP BY environment;";
        List<Fb_docker> selectArray = fbDBDao.selectArray(condition);
        List<String> envsList = new ArrayList<String>();
        for (Fb_docker fbBean : selectArray) {
            envsList.add(fbBean.getEnvironment());
        }
        CommonResultVO<List<String>> commonResultVO = new CommonResultVO<List<String>>();
        commonResultVO.setData(envsList);
        commonResultVO.setTotal(envsList.size());
        String result = StringUtils.toJSON(commonResultVO);
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/getSingleFbDB", produces = "text/html;charset=UTF-8")
    public String getingleFbDB(HttpServletResponse response, @RequestParam("build_num") String buildNum) {
        String condition = "";
        // 查询条件
        Map<String, String> map = new HashMap<String, String>();
        map.put("build_num", buildNum);
        condition += Global.spellSelectCondition(map);
        // 获得查询总条数
        int sumCount = 1;
        logger.info("sumCount:" + sumCount);
        // 查询分页
        String result = JsonUtil.beansToJson(fbDBDao.selectArray(condition), sumCount, "heihei");
        response.addHeader("Access-Control-Allow-Origin", "*");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getGitRevision", produces = "text/html;charset=UTF-8")
    public String getGitRevision(@RequestParam(value = "sourceCode", required = true) String sourceCode) {
        // 获得查询条件components,
        String condition = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("sourceCode", sourceCode);
        map.put("tar_complete_flag", "true");
        // map.put("tar_notbuild_flag", "null");
        condition += Global.spellSelectCondition(map) + "and tar_notbuild_flag is null ";
        Fb_docker fb_docker = fbDBDao.getFb_docker(condition);
        String gitRevision = "";
        String build_num = "";
        if (fb_docker != null) {
            gitRevision = fb_docker.getGit_revision();
            build_num = fb_docker.getBuild_num();
        }
        logger.info("build_num:" + build_num + "  gitRevision:" + gitRevision);
        String buildnum_gitrevision = "";
        if (gitRevision != null && !gitRevision.isEmpty() && build_num != null && !build_num.isEmpty()) {
            buildnum_gitrevision = build_num + ":" + gitRevision;
        }
        return buildnum_gitrevision;
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
        Fb_docker fb_docker = fbDBDao.getFb_docker(condition);

        String jobName = "";
        String build_id = "";
        if (fb_docker != null) {
            jobName = fb_docker.getJob_name();
            build_id = fb_docker.getBuild_id();
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
        FbDBService fbDBService = new FbDBService();
//        fbDBService.getLogInfo("792");
//        System.out.println(fbDBService.getingleFbDB(null,"MY_TON_144"));
        System.err.println(fbDBService.getEnvs());
    }
}
