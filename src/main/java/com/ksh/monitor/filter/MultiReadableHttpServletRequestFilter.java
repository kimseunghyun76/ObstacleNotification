package com.ksh.monitor.filter;

import com.ksh.monitor.util.MultiReadableHttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-05
 * Time : 오후 12:28
 *
 */
public class MultiReadableHttpServletRequestFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MultiReadableHttpServletRequest multiReadableHttpServletRequest = new MultiReadableHttpServletRequest((HttpServletRequest)servletRequest);
        filterChain.doFilter(multiReadableHttpServletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
