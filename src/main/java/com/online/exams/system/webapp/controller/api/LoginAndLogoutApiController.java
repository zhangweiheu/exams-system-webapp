package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.mybatis.enums.UserStatusEnum;
import com.online.exams.system.core.mybatis.enums.UserTypeEnum;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.NotNeedLogin;
import com.online.exams.system.webapp.bean.UserHolder;
import com.online.exams.system.webapp.bean.VO.UserVo;
import com.online.exams.system.webapp.geetest.GeetestConfig;
import com.online.exams.system.webapp.geetest.GeetestLib;
import com.online.exams.system.webapp.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhang on 2016/2/20.
 */
@RestController
@RequestMapping("/api")
public class LoginAndLogoutApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAndLogoutApiController.class);

    @Autowired
    private UserService userService;

    @NotNeedLogin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse index(@ModelAttribute UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("{}请求登陆", userVo.getUsername());
        int gtResult;
        GeetestLib geetestLib = new GeetestLib(GeetestConfig.getCaptchaId(), GeetestConfig.getPrivateKey());
        int gt_server_status_code = (Integer) request.getSession().getAttribute(geetestLib.gtServerStatusSessionKey);
        if (gt_server_status_code == 1) {
            gtResult =
                    geetestLib.enhencedValidateRequest(userVo.getGeetest_challenge(), userVo.getGeetest_validate(), userVo.getGeetest_seccode());
        } else {
            gtResult =
                    geetestLib.failbackValidateRequest(userVo.getGeetest_challenge(), userVo.getGeetest_validate(), userVo.getGeetest_seccode());
        }
        //验证码二次校验
        if (gtResult == 0) {
            return JsonResponse.failed("验证不通过");
        }
        //密码校验
        User user = userService.findUserByName(userVo.getUsername());
        if (null == user || UserStatusEnum.DELETED == user.getStatus() || UserStatusEnum.BLACK == user.getStatus()|| !user.getPassword().equals(userVo.getPassword())) {
            return JsonResponse.failed("验证不通过");
        }
        LOGGER.info("{} 通过验证登陆", user.getUsername());
        try {
            String token = TokenUtil.generateToken(user.getId() + "_" + userVo.getUsername());
            //设置登录cookies
            Cookie cookie = new Cookie(TokenUtil.TOKEN_COOKIE_KEY, token);
            //设置用户类型 cookie
            Cookie privilegeCookie = new Cookie(TokenUtil.TOKEN_PRIVILEGE_COOKIE_KEY, user.getType() != UserTypeEnum.COMMON ? "T" : "F");
            cookie.setPath("/");
            privilegeCookie.setPath("/");
            if("on".equals(userVo.getIsSavePassword())){
                cookie.setMaxAge(259200);
                privilegeCookie.setMaxAge(259200);
            }else {
                cookie.setMaxAge(7200);
                privilegeCookie.setMaxAge(7200);
            }
            response.addCookie(cookie);
            response.addCookie(privilegeCookie);
        } catch (Exception e) {
            LOGGER.warn("generate token fail");
            e.getMessage();
            return JsonResponse.failed();
        }
        return JsonResponse.success();
    }

    @RequestMapping(value = "/logout")
    public JsonResponse doLogOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue("");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        LOGGER.info("用户{}退出系统", UserHolder.getInstance().getUser().getId());
        return JsonResponse.success();
    }
}
