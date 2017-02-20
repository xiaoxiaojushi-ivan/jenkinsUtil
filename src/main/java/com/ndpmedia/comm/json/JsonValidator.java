package com.ndpmedia.comm.json;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * 
* @ClassName: JsonValidator 
* @Description: 校验一个字符串是否是合法的JSON串
* @author ivan.wang
* @date 2015-8-5 下午3:42:10 
*
 */
public class JsonValidator {

    /**
     * 验证一个字符串是否是合法的JSON串
     * 
     * @param input
     *            要验证的字符串
     * @return true-合法 ，false-非法
     */
    public static boolean validate(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }  
        try {
            JSONObject.fromObject(json);
            return true;  
        } catch (JSONException e) {
            return false;
        }
    }
}
