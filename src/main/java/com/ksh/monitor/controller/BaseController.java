package com.ksh.monitor.controller;

import com.ksh.monitor.util.MDCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-05
 * Time : 오전 10:38
 * To change this template use File | Settings | File and Code Templates.
 */

@ControllerAdvice
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);


    @RequestMapping(value="/errors/{code}")
    @ResponseBody
    public String errors(@PathVariable String code, ModelMap modelMap, HttpServletRequest httpServletRequest){
        String uri = (String) httpServletRequest.getAttribute("javax.servlet.error.request_uri");

        MDCUtil.set(MDCUtil.REQUEST_URI_MDC,uri);

        modelMap.put("errorCode",code);
        modelMap.put("errorPage",uri);

        logging(new Exception("Page Not Found"));

        return "error";
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String handleException(Throwable throwable){
        logging(throwable);
        return "error";
    }

    protected void logging(Throwable throwable){
        if(logger.isErrorEnabled()){
            if(throwable.getMessage() != null){
                logger.error(throwable.getMessage(),throwable);
            }else{
                logger.error("ERROR",throwable);
            }
        }
    }
}
