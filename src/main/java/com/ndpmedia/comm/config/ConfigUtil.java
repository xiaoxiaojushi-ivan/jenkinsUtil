package com.ndpmedia.comm.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ConfigUtil
 * @Description: 公共类，读取所有properties配置文件,存于map
 * @author ivan.wang
 * @date 2015年7月10日 下午3:10:38
 * 
 */
public class ConfigUtil {

    private static final Logger logger = LoggerFactory
            .getLogger(ConfigUtil.class);
    public static Map<String, String> map = new HashMap<String, String>();

    private static List<String> fileList = new ArrayList<String>();
    private static Properties props = new Properties();
    public static String path = "/WEB-INF/classes/resources/";
    private static String regex = ".*\\.properties";

    public ConfigUtil(String rootPath) {
        search(rootPath + path, regex);
        for (String filename : fileList) {
            getPropertiesInfoMap(filename);
        }
    }

    private static void getPropertiesInfoMap(String propertiesName) {
        try {
            props.load(new FileInputStream(propertiesName));
            Enumeration<?> en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                map.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void search(String path, String regex) {

        search(new File(path), Pattern.compile(regex));
    }

    private static void search(File folder, Pattern regex) {
        File[] files = folder.listFiles();
        try {
            if (files == null) {
                logger.error("配置路径不能访问：" + folder.getCanonicalPath());
            } else {
                for (File file : files) {
                    if (file.isDirectory()) {
                        search(file, regex);
                    } else {
                        if (regex.matcher(file.getName()).matches()) {
                            logger.info("配置文件已加载：" + file.getAbsolutePath());
                            fileList.add(file.getAbsolutePath());
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
