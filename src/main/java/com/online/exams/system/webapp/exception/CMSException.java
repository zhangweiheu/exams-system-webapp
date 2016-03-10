package com.online.exams.system.webapp.exception;

/**
 * Created by quxiao on 2015/4/17.
 */
public class CMSException extends RuntimeException {
    public CMSException() {
        this("出现异常");
    }

    public CMSException(String message) {
        super(message);
    }
}
