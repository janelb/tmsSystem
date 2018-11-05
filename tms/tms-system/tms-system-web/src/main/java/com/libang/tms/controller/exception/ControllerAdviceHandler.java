package com.libang.tms.controller.exception;

import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 没有权限
 * @author libang
 * @date 2018/8/31 11:10
 */
@ControllerAdvice
public class ControllerAdviceHandler {

    @ExceptionHandler(UnauthenticatedException.class)
    public String unauthorizedHandler(){
        return "error/401";
    }
}
