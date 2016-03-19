/**
 * 
 */
package com.online.exams.system.webapp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当解析枚举时，标识是否用枚举名称替代枚举值
 * 
 * @author tanxianwen 2015年1月13日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParseNameReplaceValue {

}
