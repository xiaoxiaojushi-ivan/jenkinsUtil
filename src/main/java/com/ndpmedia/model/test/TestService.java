package com.ndpmedia.model.test;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestService {
    private static final Logger logger = LoggerFactory
            .getLogger(TestService.class);
    @ResponseBody
    @RequestMapping(value = "/test1", produces = "text/html;charset=UTF-8")
    public String test11(HttpServletRequest request) {
        logger.info("调用业务test1");
        String result = "{\"result\":\"test1\"}";
        return result;
    }
}
