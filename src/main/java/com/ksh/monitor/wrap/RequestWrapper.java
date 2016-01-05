package com.ksh.monitor.wrap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-04
 * Time : 오후 3:44
 * HttpServletRequest를 래핑하는 클래스 이군요.
 * 특이한 점은 StringJoiner를 사용한 점.
 *
 */
public class RequestWrapper {

    private HttpServletRequest request;

    private RequestWrapper(HttpServletRequest request){
        this.request = request;
    }

    public static RequestWrapper of(HttpServletRequest request){
        return new RequestWrapper(request);
    }

    public static RequestWrapper of(ServletRequest request){
        return of((HttpServletRequest)request);
    }

    public Map<String,String> headerMap(){
        Map<String,String> convertedHeaderMap = new HashMap<>();

        Enumeration<String> headerMap = request.getHeaderNames();

        while(headerMap.hasMoreElements()){
            String name = headerMap.nextElement();
            String value = request.getHeader(name);

            convertedHeaderMap.put(name,value);
        }
        return convertedHeaderMap;
    }

    public Map<String,String> parameterMap(){
        Map<String,String> convertedParameterMap = new HashMap<>();
        Map<String,String[]> parameterMap = request.getParameterMap();

        for(String key : parameterMap.keySet()){
            String[] values = parameterMap.get(key);
            StringJoiner valueString = new StringJoiner(",");

            for(String value : values){
                valueString.add(value);
            }

            convertedParameterMap.put(key,valueString.toString());
        }
        return convertedParameterMap;
    }

    public String getRequestUri(){
        return request.getRequestURI();
    }
}
