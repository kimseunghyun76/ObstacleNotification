package com.ksh.monitor.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-05
 * Time : 오전 10:40
 * To change this template use File | Settings | File and Code Templates.
 */
@RestController
public class TestController {

    @RequestMapping(value="/api/v1/test1",method= RequestMethod.GET)
    public String responseOk(){
        return "ok";
    }

    @RequestMapping(value ="/api/v1/test2",method=RequestMethod.GET)
    public String responseFail(@RequestParam(required = false)String value){
        if(null == value || StringUtils.isEmpty(value)){
            throw new IllegalArgumentException("value is null");
        }
        return "fail";
    }

    @RequestMapping(value="/api/v1/test3",method=RequestMethod.POST)
    public String responseFail2(
            @RequestParam(required = false) String value1,
            @RequestParam(required = false) String value2,
            @RequestParam(required = false) String value3,
            @RequestParam(required = false) String value4){

        if(null == value1 || StringUtils.isEmpty(value1)){
            throw new IllegalArgumentException("value is null");
        }
        return "fail";
    }

}
