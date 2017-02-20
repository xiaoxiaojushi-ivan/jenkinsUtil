package com.ndpmedia.comm.json;

import java.sql.Timestamp;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.List;  
  
import net.sf.json.JSONArray;  
import net.sf.json.JSONObject;  
import net.sf.json.JsonConfig;  
import net.sf.json.processors.JsonValueProcessor;  
  
public class JsonValueProcessorImpl implements JsonValueProcessor {  
  
    private String format;  
  
    public JsonValueProcessorImpl(String format) {  
        super();  
        this.format = format;  
    }  
  
    public Object processArrayValue(Object arg0, JsonConfig arg1) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
    public Object processObjectValue(String key, Object value,  
            JsonConfig jsonConfig) {  
        // TODO Auto-generated method stub  
        if (value instanceof Date) {  
            String str = new SimpleDateFormat(format).format((Date) value);  
            return str;  
        }  
        if (value instanceof Timestamp) {  
            String str = new SimpleDateFormat(format).format((Timestamp) value);  
            return str;  
        }  
        return null;  
    }  
  
    /* 
     * 以下部分自行处理 
     */  
    public JsonConfig getJsonConfig() {  
        JsonConfig jsonConfig = new JsonConfig();  
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,  
                new JsonValueProcessorImpl(this.format));  
        jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class,  
                new JsonValueProcessorImpl(this.format));  
        return jsonConfig;  
    }  
  
    public String getStrByList(List obj) {  
        JSONArray jsonArray = JSONArray.fromObject(obj, this.getJsonConfig());  
        return jsonArray.toString();  
    }  
  
    public String getStrByObject(Object obj) {  
        JSONObject json = JSONObject.fromObject(obj, this.getJsonConfig());  
        return json.toString();  
    }  
}  