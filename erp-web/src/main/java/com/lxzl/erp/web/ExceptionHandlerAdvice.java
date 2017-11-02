package com.lxzl.erp.web;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.core.component.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * User : LiuKe
 * Date : 2016/12/30
 * Time : 16:37
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception exception , WebRequest request) {
        log.error("CommonExceptionHandler catche the System Exception, ", exception);
        return new ResponseEntity<Object>(resultGenerator.generate(ErrorCode.SYSTEM_ERROR,ErrorCode.getMessage(ErrorCode.SYSTEM_ERROR) ),HttpStatus.OK);
    }
    @Autowired
    private ResultGenerator resultGenerator;
}