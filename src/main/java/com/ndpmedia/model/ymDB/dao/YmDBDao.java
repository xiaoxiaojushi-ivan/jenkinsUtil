package com.ndpmedia.model.ymDB.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndpmedia.bean.Ym_JDBBean;
import com.ndpmedia.comm.Global;
import com.ndpmedia.comm.config.ConfigUtil;
import com.ndpmedia.comm.sql.DBManager;

public class YmDBDao {
    private static final Logger logger = LoggerFactory.getLogger(YmDBDao.class);
    private String sql = "";

    public List<Ym_JDBBean> selectArray(String condition) {

        List<Ym_JDBBean> attList = new ArrayList<Ym_JDBBean>();
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if (condition == null || condition.isEmpty())
            sql = "select * from ym_JDB ";
        else
            sql = "select * from ym_JDB " + condition;
        dataList = DBManager.queryArray(sql);
        logger.info(sql);
        for (Map<String, Object> map : dataList) {
            attList.add((Ym_JDBBean) Global.convertMap(Ym_JDBBean.class, map));
        }
        return attList;
    }

    @SuppressWarnings("unchecked")
    public int update(Ym_JDBBean ymBean, String condition) {
        int result = 0;
        String setString = Global.mapToSqlString(Global.convertBean(ymBean), "id", "creation_time", "build_num");
        if (!setString.equals("")) {
            sql = "update ym_JDB";
            sql += " set " + setString;
            sql += " " + condition;
            logger.info(sql);
            result = DBManager.update(sql);
        } else {
            logger.info("no update data");
        }
        return result;
    }

    public int save(Ym_JDBBean ymBean) {
        sql = "INSERT INTO ym_JDB ";
        sql += Global.valuesString(ymBean);
        logger.info(sql);
        return DBManager.update(sql);
    }

    public Ym_JDBBean getYmBean(String condition) {
        Ym_JDBBean ymBean = null;
        if (condition != null) {
            sql = "select * from ym_JDB " + condition + " ORDER BY id DESC  limit 1;";
            logger.info(sql);
            ymBean = (Ym_JDBBean) DBManager.query(Ym_JDBBean.class, sql);
        }
        return ymBean;

    }
}
