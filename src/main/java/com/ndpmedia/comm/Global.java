package com.ndpmedia.comm;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ndpmedia.bean.Fb_docker;
import com.ndpmedia.comm.json.DateJsonValueProcessor;
import com.ndpmedia.comm.json.JsonUtil;
import com.ndpmedia.model.fbDB.FbDBService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * @ClassName: Global
 * @Description: 全局公用方法
 * @author ivan.wang
 * @date 2015年7月31日 下午1:44:18
 * 
 */
public class Global {
    private static final Logger logger = LoggerFactory.getLogger(Global.class);
    static SimpleDateFormat dateformat = null;

    /**
     * 获取当前时间格式为：yyyy-mm-dd hh:mm:ss
     * */
    public static String getDateTime() {
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat.format(new Date());
    }

    /**
     * @return 获取当前时间格式为：HH:mm:ss
     */
    public static String getTime() {
        dateformat = new SimpleDateFormat("HH:mm:ss");
        return dateformat.format(new Date());
    }

    /**
     * @return 获取当前时间格式为：yyyy-mm-dd
     */
    public static String getDate() {
        dateformat = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(new Date());
    }

    /**
     * 获取当天是星期几
     */
    public static String getWeekDay() {
        final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    /**
     * string 转 Timestamp 类型
     * 
     * @param datetime
     * @return
     */
    public static Timestamp stringExDatetime(String datetime) {
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateformat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Timestamp(date.getTime());
    }

    /**
     * Timestamp 转 string类型
     * 
     * @param datetime
     * @return
     */
    public static String timestampExString(Timestamp timestamp) {
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat.format(timestamp);
    }

    /**
     * string 转java.sql. Date 类型
     * 
     * @param date
     * @return
     */
    public static java.sql.Date stringExDate(String date) {
        dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date da = null;
        try {
            da = dateformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(da.getTime());
    }

    /**
     * string 转 java.sql.Time 类型
     * 
     * @param time
     * @return
     */
    public static java.sql.Time stringExTime(String time) {
        dateformat = new SimpleDateFormat("HH:mm:ss");
        Date da = null;
        try {
            da = dateformat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Time(da.getTime());
    }

    /**
     * @param date
     * @return 获取指定时间,格式为：HH:mm:ss
     */
    public static String getAppointTime(Date date) {
        dateformat = new SimpleDateFormat("HH:mm:ss");
        return dateformat.format(date);
    }

    /**
     * @param date
     * @return 获取指定日期,格式为：yyyy-mm-dd
     */
    public static String getAppointDate(Date date) {
        dateformat = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(date);
    }

    /**
     * @param date
     * @return 获取指定日期时间,格式为：yyyy-mm-dd
     */
    public static String getAppointDateTime(Date date) {
        dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat.format(date);
    }

    /**
     * 用于更新数据的因为更新的内容个数不定 所以在sql 语句中的set个数不好确定 采用map解决个数不定的问题
     * 
     * @param map
     * @param mapExclude
     *            要排除的字段
     * @return
     */
    public static String mapToSqlString(Map<String, Object> map, String... mapExclude) {
        String sql = " ";
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            if (!entry.getValue().equals("") && !Arrays.asList(mapExclude).contains(entry.getKey())) {
                sql += entry.getKey() + "='" + entry.getValue() + "',";
            }
        }
        return sql.substring(0, sql.length() - 1);
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     * 
     * @param bean
     *            要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map convertBean(Object bean) {
        Map returnMap = null;
        try {
            Class type = bean.getClass();
            returnMap = new HashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
            logger.debug(returnMap.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * 
     * @param type
     *            要转化的类型
     * @param map
     *            包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static Object convertMap(Class type, Map map) {
        Object obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
            obj = type.newInstance();

            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();

                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    Method method = descriptor.getWriteMethod();
                    method.invoke(obj, args);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String beansToJson(List<?> beans, int sumCount) {
        return JsonUtil.beansToJson(beans, sumCount, "");
    }

    public static String decodeStr(String str) {
        try {
//             return new String(str.getBytes("ISO-8859-1"), "UTF-8");
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String spellSelectCondition(Map<String, String> map) {
        String sql = " where ";
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                String keyString = decodeStr(entry.getKey());
                if (keyString.equals("id")) {
                    Object it = entry.getValue();
                    if (it.toString().equals("0")) {
                        continue;
                    } else {
                        sql += keyString + "=" + it.toString() + " and ";
                        continue;
                    }
                }
                if (keyString.equals("build_num")) {
                    Object it = entry.getValue();
                    if (it.toString().equals("")) {
                        continue;
                    } else {
                        sql += keyString + "='" + it.toString() + "' and ";
                        continue;
                    }
                }
                if (!entry.getValue().equals("")) {
                    sql += keyString + " like '%" + decodeStr(entry.getValue()) + "%'";
                    sql += " and ";
                }
            }
            if (map.size() == 0) {
                sql += "";
            } else {
                if (!sql.equals(" where ")) {
                    sql = sql.substring(0, sql.length() - 4);
                }else {
                    sql = "";
                }
            }
        }
        return sql;
    }

    public static String limitPageString(String page, String pageCapacity) {
        String limitString = "";
        // 查询分页
        if (!page.equals("") && !pageCapacity.equals("")) {
            int p = (Integer.parseInt(page) - 1) * Integer.parseInt(pageCapacity);
            limitString = " limit " + p + "," + Integer.parseInt(pageCapacity);
        }
        return limitString;
    }

    public static String valuesString(Object fbDocker) {
        String valuesString = " set ";
        try {
            Field[] fields = fbDocker.getClass().getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                String fieldName = fields[j].getName();
                Class<?> typeClass = fields[j].getType();
                String firstLetter = fieldName.substring(0, 1).toUpperCase();
                String getter = "get" + firstLetter + fieldName.substring(1);
                Method method = fbDocker.getClass().getMethod(getter, new Class[] {});
                Object fieldValue = method.invoke(fbDocker, new Object[] {});
                if (fieldValue != null && !fieldName.equals("id")) {
                    String value = fieldValue.toString();
                    if (typeClass.getName().equals("java.sql.Timestamp")) {
                        value = Global.timestampExString((Timestamp) fieldValue);
                    }
                    valuesString += fieldName + "='" + value + "',";
                }
            }
            valuesString = valuesString.substring(0, valuesString.length() - 1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return valuesString;
    }
}
