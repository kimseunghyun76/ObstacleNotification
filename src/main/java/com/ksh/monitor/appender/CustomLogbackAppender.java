package com.ksh.monitor.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.util.ContextUtil;
import com.ksh.monitor.config.LogConfig;
import com.ksh.monitor.entity.ErrorLog;
import com.ksh.monitor.entity.ErrorLogService;
import com.ksh.monitor.util.JsonUtils;
import com.ksh.monitor.util.MDCUtil;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-05
 * Time : 오후 2:13
 * To change this template use File | Settings | File and Code Templates.
 */
public class CustomLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent>{

    private ErrorLogService errorLogService;

    private LogConfig logConfig;

    public CustomLogbackAppender() {
    }


    public CustomLogbackAppender(ErrorLogService errorLogService, LogConfig logConfig){
        this.errorLogService = errorLogService;
        this.logConfig = logConfig;
    }


    @Override
    public void doAppend(ILoggingEvent eventObject){
        super.doAppend(eventObject);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if(eventObject.getLevel().isGreaterOrEqual(logConfig.getLevel())){
            ErrorLog errorLog = getErrorLog(eventObject);

            if(logConfig.getDatabase().isEnabled()){
                if(logConfig.getSlack().isEnabled()){
                    errorLog.setAlertYn("Y");
                }
                toDatabase(errorLog);
            }

            if(logConfig.getSlack().isEnabled()){
                toSlack(errorLog);
            }

        }
    }

    private void toDatabase(ErrorLog errorLog) {
        System.out.println("DB에 저장이 되느냐?");
        System.out.println("DB에 저장이 되느냐?");
        System.out.println("DB에 저장이 되느냐?");
        System.out.println("DB에 저장이 되느냐?");
        errorLogService.save(errorLog);
    }
    private ErrorLog getErrorLog(ILoggingEvent loggingEvent){
        if(loggingEvent.getLevel().isGreaterOrEqual(logConfig.getLevel())){
            ErrorLog errorLog = new ErrorLog();
            errorLog.setPhase("alpha");
            errorLog.setSystem("testSystem");
            errorLog.setLoggerName(loggingEvent.getLoggerName());
            errorLog.setServerName("localhost");
            errorLog.setHostName(getHostName());
            errorLog.setPath(MDCUtil.get(MDCUtil.REQUEST_URI_MDC));
            errorLog.setMessage(loggingEvent.getFormattedMessage());
            errorLog.setHeaderMap(MDCUtil.get(MDCUtil.HEADER_MAP_MDC));
            errorLog.setParameterMap(MDCUtil.get(MDCUtil.PARAMETER_MAP_MDC));
            errorLog.setUserInfo(MDCUtil.get(MDCUtil.USER_INFO_MDC));
            errorLog.setAgentDetail(MDCUtil.get(MDCUtil.AGENT_DETAIL_MDC));

            if(loggingEvent.getThrowableProxy() != null){
                errorLog.setTrace(getStackTrace(loggingEvent.getThrowableProxy().getStackTraceElementProxyArray()));
            }

            return errorLog;
        }
        return null;
    }

    private String getStackTrace(StackTraceElementProxy[] stackTraceElementProxyArray) {
        if(stackTraceElementProxyArray == null || stackTraceElementProxyArray.length == 0){
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for(StackTraceElementProxy element : stackTraceElementProxyArray){
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getHostName() {
        try{
            return ContextUtil.getLocalHostName();
        }catch (Exception e){
            //ignored
        }
        return null;
    }



    private void toSlack(ErrorLog errorLog) {
        SlackApi slackApi = new SlackApi(logConfig.getSlack().getWebHookUrl());

        List<SlackField> fields = new ArrayList<>();

        SlackField message = new SlackField();
        message.setTitle("에러내용");
        message.setValue(errorLog.getMessage());
        message.setShorten(false);
        fields.add(message);

        SlackField path = new SlackField();
        path.setTitle("요청 URL");
        path.setValue(errorLog.getPath());
        path.setShorten(false);
        fields.add(path);

        SlackField date = new SlackField();
        date.setTitle("발생시간");
        date.setValue(errorLog.getErrorDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        date.setShorten(true);
        fields.add(date);

        SlackField profile = new SlackField();
        profile.setTitle("프로파일");
        profile.setValue(errorLog.getPhase());
        profile.setShorten(true);
        fields.add(profile);

        SlackField system = new SlackField();
        system.setTitle("시스템명");
        system.setValue(errorLog.getSystem());
        system.setShorten(true);
        fields.add(system);

        SlackField serverName = new SlackField();
        serverName.setTitle("서버명");
        serverName.setValue(errorLog.getServerName());
        serverName.setShorten(true);
        fields.add(serverName);

        SlackField hostName = new SlackField();
        hostName.setTitle("호스트명");
        hostName.setValue(errorLog.getHostName());
        hostName.setShorten(false);
        fields.add(hostName);

        SlackField userInformation = new SlackField();
        userInformation.setTitle("사용자 정보");
        userInformation.setValue(JsonUtils.toPrettyJson(errorLog.getUserInfo()));
        userInformation.setShorten(false);
        fields.add(userInformation);

        SlackField headerInformation = new SlackField();
        headerInformation.setTitle("Http Header 정보");
        headerInformation.setValue(JsonUtils.toPrettyJson(errorLog.getHeaderMap()));
        headerInformation.setShorten(false);
        fields.add(headerInformation);

        SlackField bodyInformation = new SlackField();
        bodyInformation.setTitle("Http Body 정보");
        bodyInformation.setValue(JsonUtils.toPrettyJson(errorLog.getParameterMap()));
        bodyInformation.setShorten(false);
        fields.add(bodyInformation);

        SlackField agentDetail = new SlackField();
        agentDetail.setTitle("사용자 환경정보");
        agentDetail.setValue(JsonUtils.toPrettyJson(errorLog.getAgentDetail()));
        agentDetail.setShorten(false);
        fields.add(agentDetail);


        String title = errorLog.getMessage();

        if(logConfig.getDatabase().isEnabled()){
            title = "상세 로그 보기 / [" + errorLog.getId() + "] ";
        }

        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.setFallback("에러발생!! 확인이 필요합니다.");
        slackAttachment.setColor("danger");
        slackAttachment.setFields(fields);
        slackAttachment.setTitle(title);
        slackAttachment.setTitleLink("http://log.test.com");
        slackAttachment.setText(errorLog.getTrace());

        SlackMessage slackMessage = new SlackMessage("");
        slackMessage.setChannel("#" + logConfig.getSlack().getChannel());
        slackMessage.setIcon(":exclamation:");
        slackMessage.setAttachments(Collections.singletonList(slackAttachment));

        slackApi.call(slackMessage);
    }


}
