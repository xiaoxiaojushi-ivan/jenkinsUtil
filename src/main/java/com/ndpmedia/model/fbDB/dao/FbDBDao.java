package com.ndpmedia.model.fbDB.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndpmedia.bean.Fb_docker;
import com.ndpmedia.comm.Global;
import com.ndpmedia.comm.config.ConfigUtil;
import com.ndpmedia.comm.sql.DBManager;

public class FbDBDao {
    private static final Logger logger = LoggerFactory.getLogger(FbDBDao.class);
    private String sql = "";

    public List<Fb_docker> selectArray(String condition) {

        List<Fb_docker> attList = new ArrayList<Fb_docker>();
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if (condition == null || condition.isEmpty())
            sql = "select * from fb_docker ";
        else
            sql = "select * from fb_docker " + condition;
        dataList = DBManager.queryArray(sql);
        logger.info(sql);
        for (Map<String, Object> map : dataList) {
            attList.add((Fb_docker) Global.convertMap(Fb_docker.class, map));
        }
        return attList;
    }

    @SuppressWarnings("unchecked")
    public int update(Fb_docker fbdDocker, String condition) {
        int result = 0;
        String setString = Global.mapToSqlString(Global.convertBean(fbdDocker), "id", "creation_time", "environment",
                "build_num");
        if (!setString.equals("")) {
            sql = "update fb_docker";
            sql += " set " + setString;
            sql += " " + condition;
            logger.info(sql);
            result = DBManager.update(sql);
        } else {
            logger.info("no update data");
        }
        return result;
    }

    public int save(Fb_docker fbdDocker) {
        sql = "INSERT INTO fb_docker ";
        sql += Global.valuesString(fbdDocker);
        logger.info(sql);
        return DBManager.update(sql);
    }
    
    public Fb_docker getFb_docker(String condition){
        Fb_docker fbBean = null;
        if (condition!=null) {
            sql = "select * from fb_docker " + condition + " ORDER BY id DESC  limit 1;";
            logger.info(sql);
            fbBean = (Fb_docker) DBManager.query(Fb_docker.class, sql);
        }
        return fbBean;
        
    }

    public static void main(String[] args) {
        ConfigUtil.path="\\main\\resources\\resources\\sql";
        ConfigUtil configUtil = new ConfigUtil("D:\\workspace\\jenkinsUtil\\src");
        FbDBDao dao = new FbDBDao();
        dao.getFb_docker("build_num='BOff_18'");
    }
}
