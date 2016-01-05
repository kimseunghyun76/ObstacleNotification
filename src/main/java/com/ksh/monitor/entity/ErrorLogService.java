package com.ksh.monitor.entity;

import com.ksh.monitor.entity.ErrorLog;
import com.ksh.monitor.entity.ErrorLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-05
 * Time : 오후 2:39
 * To change this template use File | Settings | File and Code Templates.
 */
@Service
public class ErrorLogService {

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Transactional
    public void save(ErrorLog errorLog){
        errorLogRepository.save(errorLog);
    }
}
