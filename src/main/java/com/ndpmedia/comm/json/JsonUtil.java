package com.ndpmedia.comm.json;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonUtil {

    /**
     * 将beans转化为特定模式的json字符串
     * @param beans
     * @param sumCount
     * @param message
     * @return
     */
    public static String beansToJson(List<?> beans, int sumCount, String message) {
        JSONObject jsonBeans = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonBeans.put("sumCount", sumCount);
        jsonBeans.put("count", beans.size());
        boolean result = false;
        if (beans.size() != 0) {
            result = true;
        }
        jsonBeans.put("success", result);

        JsonConfig config = new JsonConfig();
        JSONObject json = null;
        for (int i = 0; i < beans.size(); i++) {
            Object obj = beans.get(i);
            config.registerJsonValueProcessor(Timestamp.class,
                    new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
            config.registerJsonValueProcessor(java.sql.Date.class,
                    new DateJsonValueProcessor("yyyy-MM-dd"));
            config.registerJsonValueProcessor(java.sql.Time.class,
                    new DateJsonValueProcessor("HH:mm:ss"));
            json = JSONObject.fromObject(obj, config);
            jsonArray.add(i, json);
        }
        jsonBeans.put("data", jsonArray);
        jsonBeans.put("code", 200);
        jsonBeans.put("message", message);

        return jsonBeans.toString();
    }

    /**
     * 根据key值，在json字符串中解析出对应的value值
     * 
     * @param jsonString
     * @param key
     * @return
     */
    public static String analysisJson(String jsonString, String key) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Iterator<?> iterator = jsonObject.keys();
        String keyString = null;
        String valueString = null;
        boolean isWhile = false;
        while (iterator.hasNext()) {
            keyString = (String) iterator.next();
            if (key.equals(keyString)) {
                valueString = jsonObject.getString(keyString);
                break;
            } else {
                String json = jsonObject.getString(keyString);
                if (JsonValidator.validate(json)) {
                    valueString = analysisJson(json, key);
                    if (valueString != null) {
                        isWhile = true;
                    }
                }
                if (isWhile) {
                    break;
                }
            }

        }
        return valueString;
    }

    /**
     * 将Json对象转换成Map
     * 
     * @param jsonObject
     *            json对象
     * @return Map对象
     * @throws JSONException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map toMap(String jsonString) throws JSONException {

        JSONObject jsonObject = JSONObject.fromObject(jsonString);

        Map result = new HashMap();
        Iterator iterator = jsonObject.keys();
        String key = null;
        String value = null;

        while (iterator.hasNext()) {

            key = (String) iterator.next();
            value = jsonObject.getString(key);
            result.put(key, value);

        }
        return result;

    }
}
