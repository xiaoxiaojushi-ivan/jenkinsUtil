package com.ndpmedia.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ndpmedia.comm.config.ConfigUtil;
/**
 * 
* @ClassName: InstantiationConfig 
* @Description: 初始化配置管理
* @author ivan.wang
* @date 2015-8-11 下午1:54:57 
*
 */
@Component("InstantiationConfig")
public class InstantiationConfig implements
        ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory
            .getLogger(InstantiationConfig.class);

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            // 初始化配置
            String rootPath = System.getProperty("webapp.root");
            logger.info("rootPath:" + rootPath);
            new ConfigUtil(rootPath);
        }
    }
}
