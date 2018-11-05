package com.libang.tms.exception;

/**
 * @author libang
 * @date 2018/8/31 17:36
 */
public class ServiceException extends  RuntimeException {

    public ServiceException(){}
    public ServiceException(String message){
            super(message);
    }
    public ServiceException(Throwable th){
        super(th);

    }

    public ServiceException(String message,Throwable th){
        super(message,th);

    }

}
