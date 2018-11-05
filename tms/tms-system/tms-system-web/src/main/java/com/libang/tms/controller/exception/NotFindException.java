package com.libang.tms.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * 404
 * @author libang
 * @date 2018/8/31 11:08
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFindException extends RuntimeException{
}
