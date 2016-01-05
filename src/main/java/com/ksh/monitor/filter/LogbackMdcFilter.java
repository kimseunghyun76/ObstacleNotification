package com.ksh.monitor.filter;


import com.ksh.monitor.util.AgentUtils;
import com.ksh.monitor.util.HttpUtils;
import com.ksh.monitor.util.MDCUtil;
import com.ksh.monitor.wrap.RequestWrapper;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-04
 * Time : 오후 2:56
 * To change this template use File | Settings | File and Code Templates.
 */
public class LogbackMdcFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(LogbackMdcFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestWrapper requestWrapper = RequestWrapper.of(servletRequest);

        //Set Http Header
        MDCUtil.setJsonValue(MDCUtil.HEADER_MAP_MDC, requestWrapper.headerMap());


        //Set Http Body
        MDCUtil.setJsonValue(MDCUtil.PARAMETER_MAP_MDC, requestWrapper.parameterMap());

        //If you use SpringSecurity, you could use SpringSecurity UserDetail Information.
        MDCUtil.setJsonValue(MDCUtil.USER_INFO_MDC , HttpUtils.getCurrentUser());

        //Set Agent Detail
        MDCUtil.setJsonValue(MDCUtil.AGENT_DETAIL_MDC, AgentUtils.getAgentDetail((HttpServletRequest)servletRequest));

        //Set Http Request URI
        MDCUtil.set(MDCUtil.REQUEST_URI_MDC, requestWrapper.getRequestUri());

        try{
            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            MDC.clear();
        }

    }

    @Override
    public void destroy() {

    }
}
