package com.online.exams.system.webapp.Interceptor;

import com.online.exams.system.core.model.User;
import com.online.exams.system.core.mybatis.enums.UserTypeEnum;
import com.online.exams.system.webapp.annotation.AdminOnly;
import com.online.exams.system.webapp.annotation.ManagerOnly;
import com.online.exams.system.webapp.bean.UserHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制权限
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class PrivilegeInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AdminOnly adminOnly = null;
        ManagerOnly managerOnly = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            adminOnly = method.getMethodAnnotation(AdminOnly.class);
            managerOnly = method.getMethodAnnotation(ManagerOnly.class);

            if (adminOnly == null) {
                adminOnly = method.getBeanType().getAnnotation(AdminOnly.class);
            }
            if (managerOnly == null) {
                managerOnly = method.getBeanType().getAnnotation(ManagerOnly.class);
            }
        }

        if (adminOnly == null && managerOnly == null) {
            // 不需要管理员权限，放过检查
            return true;
        }

        User user = UserHolder.getInstance().getUser();
        if (user != null) {
            if (UserTypeEnum.ADMIN == user.getType()) {
                return true;
            }
            if (UserTypeEnum.MANAGER == user.getType() && adminOnly == null) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 将参数值中的“[]”进行转义
     *
     * @param value
     * @return
     */
    public static String escape(String value) {
        if (StringUtils.isNotBlank(value)) {
            return value.replace("[", "\\[").replace("]", "\\]");
        }
        return "";
    }
}
