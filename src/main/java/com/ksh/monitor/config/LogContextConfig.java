package com.ksh.monitor.config;

import ch.qos.logback.classic.LoggerContext;
import com.ksh.monitor.appender.CustomLogbackAppender;
import com.ksh.monitor.entity.ErrorLogService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-05
 * Time : 오후 3:20
 * To change this template use File | Settings | File and Code Templates.
 */
@Configuration
public class LogContextConfig implements InitializingBean{

    @Autowired
    private LogConfig logConfig;

    @Autowired
    private ErrorLogService errorLogService;

    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        CustomLogbackAppender customLogbackAppender = new CustomLogbackAppender(errorLogService,logConfig);

        customLogbackAppender.setContext(loggerContext);
        customLogbackAppender.setName("customLogbackAppender");
        customLogbackAppender.start();
        loggerContext.getLogger("ROOT").addAppender(customLogbackAppender);

    }
}
