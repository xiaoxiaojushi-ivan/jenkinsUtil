package com.ndpmedia.model.gktDB.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndpmedia.bean.Gkt_JDBBean;
import com.ndpmedia.comm.Global;
import com.ndpmedia.comm.sql.DBManager;

public class GKTDBDao {
    private static final Logger logger = LoggerFactory.getLogger(GKTDBDao.class);
    private String sql = "";

    public List<Gkt_JDBBean> selectArray(String condition) {

        List<Gkt_JDBBean> attList = new ArrayList<Gkt_JDBBean>();
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if (condition == null || condition.isEmpty())
            sql = "select * from gkt_JDB ";
        else
            sql = "select * from gkt_JDB " + condition;
        dataList = DBManager.queryArray(sql);
        logger.info(sql);
        for (Map<String, Object> map : dataList) {
            attList.add((Gkt_JDBBean) Global.convertMap(Gkt_JDBBean.class, map));
        }
        return attList;
    }

    @SuppressWarnings("unchecked")
    public int update(Gkt_JDBBean ymBean, String condition) {
        int result = 0;
        String setString = Global.mapToSqlString(Global.convertBean(ymBean), "id", "creation_time", "build_num");
        if (!setString.equals("")) {
            sql = "update gkt_JDB";
            sql += " set " + setString;
            sql += " " + condition;
            logger.info(sql);
            result = DBManager.update(sql);
        } else {
            logger.info("no update data");
        }
        return result;
    }

    public int save(Gkt_JDBBean ymBean) {
        sql = "INSERT INTO gkt_JDB ";
        sql += Global.valuesString(ymBean);
        logger.info(sql);
        return DBManager.update(sql);
    }

    public Gkt_JDBBean getGktBean(String condition) {
        Gkt_JDBBean ymBean = null;
        if (condition != null) {
            sql = "select * from gkt_JDB " + condition + " ORDER BY id DESC  limit 1;";
            logger.info(sql);
            ymBean = (Gkt_JDBBean) DBManager.query(Gkt_JDBBean.class, sql);
        }
        return ymBean;

    }
}
