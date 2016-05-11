package com.online.exams.system.webapp.Interceptor;

import com.alibaba.fastjson.JSON;
import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.mybatis.enums.UserStatusEnum;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.NotNeedLogin;
import com.online.exams.system.webapp.bean.UserHolder;
import com.online.exams.system.webapp.util.TokenUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginCheckInterceptor.class);

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断是否需要跳过登录验证
        NotNeedLogin notNeedLogin = null;
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            notNeedLogin = method.getMethodAnnotation(NotNeedLogin.class);

            if (notNeedLogin == null)
                notNeedLogin = method.getBeanType().getAnnotation(NotNeedLogin.class);
        }

        if (notNeedLogin != null) {
            logger.debug("Not Need Login Check");
            return true;
        }

        // 验证登录token是否正确
        Cookie lid = getTokenCookie(request.getCookies(), TokenUtil.TOKEN_COOKIE_KEY);
        if (lid != null) {
            String token = lid.getValue();
            boolean validToken = TokenUtil.isValidToken(token);
            if (validToken) {
                Integer uid = getUid(token);
                if (uid != null) {
                    User user = userService.findUserByUid(uid);
                    if (null != user && UserStatusEnum.DELETED != user.getStatus() && UserStatusEnum.BLACK != user.getStatus()) {
                        UserHolder.getInstance().set(user);
                        return true;
                    }
                }
            }
        }

        boolean retJson = false;
        // 判断是否返回json格式
        RestController restController = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            restController = method.getMethodAnnotation(RestController.class);

            if (restController == null)
                restController = method.getBeanType().getAnnotation(RestController.class);
        }

        if (restController != null) {
            retJson = true;
        }

        logger.info("loginCheckFail");
        if (retJson) {
            response.getOutputStream().write(JSON.toJSONString(JsonResponse.failed("请登录")).getBytes("UTF-8"));
        } else {
            response.sendRedirect("");
        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //        logger.info("removing user");
        UserHolder.getInstance().delete();
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

    public Cookie getTokenCookie(Cookie[] cookies, String keyName) {
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (keyName.equalsIgnoreCase(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private Integer getUid(String token) {
        String s[] = token.split("\\|");
        if (s.length == 3) {
            String[] split = StringUtils.split(s[1], "_");
            if (split != null && split.length == 2) {
                return Integer.parseInt(split[0]);
            }
        }
        return null;
    }
}
