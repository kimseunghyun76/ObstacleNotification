package com.ksh.monitor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-04
 * Time : 오후 3:05
 * MDC : Mapped Diagnostic Context (Diagnostic : 특징적인, 증상을 나타내는)
 * ThreadLocal에 데이터 넣고, 로그 출력 시점에 넣은 데이터를 매핑해주는 유틸
 */
public class MDCUtil {

    private static Logger logger = LoggerFactory.getLogger(MDCUtil.class);

    private static MDCAdapter mdc = MDC.getMDCAdapter();

    public static final String HEADER_MAP_MDC = "HEADER_MAP_MDC";
    public static final String PARAMETER_MAP_MDC = "PARAMETER_MAP_MDC";
    public static final String USER_INFO_MDC = "USER_INFO_MDC";
    public static final String REQUEST_URI_MDC = "REQUEST_URI_MDC";
    public static final String AGENT_DETAIL_MDC = "AGENT_DETAIL_MDC";

    public static void set(String key,String value){
        mdc.put(key,value);
    }

    public static void setJsonValue(String key,Object value){
        try{
            if(value != null){
                String json = JsonUtils.toJson(value);
                mdc.put(key,json);
            }
        }catch (Exception e){
            //ignored
        }
    }
    public static String get(String key){
        return mdc.get(key);
    }

    public static void clear(){
        MDC.clear();
    }

    public static void setErrorAttribute(Map<String,Object> errorAttribute){
        if(errorAttribute.containsKey("path")){
            set(REQUEST_URI_MDC,(String)errorAttribute.get("path"));
        }
    }
}
