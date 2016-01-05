package com.ksh.monitor.entity;

import com.ksh.monitor.entity.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2016-01-05
 * Time : 오후 2:43
 * To change this template use File | Settings | File and Code Templates.
 */
@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long>{
}
