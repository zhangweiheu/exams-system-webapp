package com.online.exams.system.webapp.exception;

/**
 * Created by quxiao on 2015/4/17.
 */
public class OnlineException extends RuntimeException {
    public OnlineException() {
        this("出现异常");
    }

    public OnlineException(String message) {
        super(message);
    }
}
