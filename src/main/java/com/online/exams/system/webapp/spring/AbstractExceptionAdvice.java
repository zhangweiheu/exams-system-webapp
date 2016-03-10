package com.online.exams.system.webapp.spring;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.webapp.exception.BusinessException;
import com.online.exams.system.webapp.exception.LoggedBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.io.IOException;

/**
 * 统一错误处理的抽象类，之类可以继承该类以指定匹配的controller范围
 *
 * @author tanxianwen
 *         2015年4月23日
 */
public abstract class AbstractExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExceptionAdvice.class);

    /**
     * 处理参数校验失败的结果
     *
     * @param ex
     * @return
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseBody
    public JsonResponse handleBindException(Exception ex) {
        BindingResult result = null;
        if (ex instanceof BindException) {
            result = ((BindException) ex).getBindingResult();
        } else if (ex instanceof MethodArgumentNotValidException) {
            result = ((MethodArgumentNotValidException) ex).getBindingResult();
        } else {
            ConstraintViolationException e = (ConstraintViolationException) ex;
            return JsonResponse.failed(e.getConstraintViolations().iterator().next().getMessage());
        }
        return JsonResponse.failed(result);
    }

    /**
     * 处理所有业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public JsonResponse handleException(BusinessException ex) {
        LOGGER.warn("出现业务异常", ex);
        return JsonResponse.failed(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理所有业务异常，并记录日志
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(LoggedBusinessException.class)
    @ResponseBody
    public JsonResponse handleException(LoggedBusinessException ex) {
        LOGGER.warn("出现业务异常", ex);
        return JsonResponse.failed(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, TypeMismatchException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public JsonResponse handleExceptionForBadRequest(Exception ex) {
        // LOGGER.warn("提交的请求数据异常", ex);
        return JsonResponse.failed("提交的请求不符合要求");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public JsonResponse handleException(HttpRequestMethodNotSupportedException ex) {
        // LOGGER.warn("提交的HTTP METHOD不支持", ex);
        return JsonResponse.failed("不支持的HTTP方法：" + ex.getMethod());
    }

    /**
     * 处理IO其他异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public JsonResponse handleException(IOException ex) {
        return JsonResponse.failed("网络出错！");
    }

    /**
     * 处理其他异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResponse handleException(Exception ex) {
        LOGGER.error("出现错误", ex);
        return JsonResponse.failed(ex.getMessage());
    }

}
