package com.online.exams.system.webapp.spring;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * 支持jsonp,继承该类的子类需要指定匹配的controller的规则
 *
 * @author tanxianwen
 *         2015年2月10日
 */
public abstract class AbstractJsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public AbstractJsonpAdvice() {
        super("callback");
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (FastJsonHttpMessageConverter.class.isAssignableFrom(converterType)) {
            return true;//采用自定义的json解析框架
        }
        return super.supports(returnType, converterType);
    }
}
