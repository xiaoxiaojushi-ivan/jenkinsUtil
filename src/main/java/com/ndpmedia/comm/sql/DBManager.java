package com.ndpmedia.comm.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndpmedia.comm.Global;
import com.ndpmedia.comm.config.ConfigUtil;
import com.ndpmedia.model.fbDB.dao.FbDBDao;

/**
 * @ClassName: DBManager
 * @Description: TODO
 * @author ivan.wang
 * @date 2015年7月13日 上午10:12:31
 * 
 */
public class DBManager {
    private static final Logger logger = LoggerFactory.getLogger(DBManager.class);

    /**
     * 连接数据库的方法
     * 
     * @return conn 返回一个连接对象
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName(ConfigUtil.map.get("dbDriverName"));
        conn = DriverManager.getConnection(ConfigUtil.map.get("dbUrl"), ConfigUtil.map.get("dbUser"),
                ConfigUtil.map.get("dbPassword"));
        return conn;
    }

    // /**
    // * 本函数用来执行用户传入的sql语句(仅限于select语句)
    // *
    // * @param sql
    // * 传入的sql语句，等待执行
    // * @return 返回执行sql语句后的结果集对象
    // */
    // public static ResultSet query(String sql) {
    // Connection dbConn = null;
    // Statement stmt = null;
    // ResultSet result = null;
    // try {
    // dbConn = getConnection();
    // stmt = dbConn.createStatement();
    // result = stmt.executeQuery(sql);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return result;
    // }

    /**
     * 本方法用来执行更新语句，并返回影响了多少行（insert,update,delete）
     * 
     * @param sql
     *            传入的sql语句
     * 
     * @return 返回执行成功的记录的条数
     */
    public static int update(String sql) {
        Connection dbConn = null;
        Statement stmt = null;
        int i = 0;
        try {
            dbConn = getConnection();
            stmt = dbConn.createStatement();
            i = stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(dbConn, stmt, null);
        }
        return i;
    }

    public static Object query(Class<?> type, String sql) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Connection dbConn = null;
        Statement stmt = null;
        ResultSet result = null;
        Object obj = null;
        try {
            dbConn = getConnection();
            stmt = dbConn.createStatement();
            result = stmt.executeQuery(sql);
            Map<String, Object> map = null;
            ResultSetMetaData row = null;
            if (result.next()) {
                row = result.getMetaData();
                map = new HashMap<String, Object>();
                String key = "";
                Object value = null;
                for (int i = 0; i < row.getColumnCount(); i++) {
                    key = row.getColumnName(i + 1);
                    logger.debug("key:" + key);
                    value = result.getObject(key);
                    map.put(key, value);
                }
                dataList.add(map);
            }
            for (Map<String, Object> map2 : dataList) {
                obj = Global.convertMap(type, map2);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(dbConn, stmt, result);
        }
        return obj;
    }

    /**
     * 
     * @param sql为传入的sql语句
     * @return查询结果的List<Map<String, Object>>
     */
    public static List<Map<String, Object>> queryArray(String sql) {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Connection dbConn = null;
        Statement stmt = null;
        ResultSet result = null;
        try {
            dbConn = getConnection();
            stmt = dbConn.createStatement();
            result = stmt.executeQuery(sql);
            Map<String, Object> map = null;
            ResultSetMetaData row = null;
            while (result.next()) {
                row = result.getMetaData();
                map = new HashMap<String, Object>();
                String key = "";
                Object value = null;
                for (int i = 0; i < row.getColumnCount(); i++) {
                    key = row.getColumnName(i + 1);
                    value = result.getObject(key);
                    map.put(key, value);
                }
                dataList.add(map);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(dbConn, stmt, result);
        }
        return dataList;
    }

    public static void close(Connection conn, Statement stmt, ResultSet result) {
        try {
            if (result != null) {
                result.close();
                result = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}