package com.online.exams.system.webapp.controller;

import com.online.exams.system.core.model.User;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.bean.LoginVo;
import com.online.exams.system.webapp.geetest.GeetestConfig;
import com.online.exams.system.webapp.geetest.GeetestLib;
import com.online.exams.system.webapp.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangwei on 16/1/13.
 */
@Controller
@RequestMapping("/")
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String index(@ModelAttribute LoginVo loginVo, HttpServletRequest request, HttpServletResponse response, ModelMap view) {
        LOGGER.info("{}请求登陆", loginVo.getUsername());
        int gtResult;
        GeetestLib geetestLib = new GeetestLib(GeetestConfig.getCaptchaId(), GeetestConfig.getPrivateKey());
        int gt_server_status_code = (Integer) request.getSession().getAttribute(geetestLib.gtServerStatusSessionKey);
        if (gt_server_status_code == 1) {
            gtResult =
                    geetestLib.enhencedValidateRequest(loginVo.getGeetest_challenge(), loginVo.getGeetest_validate(), loginVo.getGeetest_seccode());
        } else {
            gtResult =
                    geetestLib.failbackValidateRequest(loginVo.getGeetest_challenge(), loginVo.getGeetest_validate(), loginVo.getGeetest_seccode());
        }
        if (gtResult == 0) {
            return "login";
        }
        User user = userService.findUserByName(loginVo.getUsername());
        if (null == user || !user.getPassword().equals(loginVo.getPassword())) {
            return "login";
        }
        LOGGER.info("{} 通过验证登陆", user.getUsername());
        try {
            //设置登录cookies
            String token = TokenUtil.generateToken(user.getId() + "_" + loginVo.getUsername());
            Cookie cookie = new Cookie(TokenUtil.TOKEN_COOKIE_KEY, token);
            cookie.setMaxAge(-1);
            cookie.setPath("/");
            response.addCookie(cookie);
            //设置用户类型 cookie
            Cookie privilegeCookie = new Cookie(TokenUtil.TOKEN_PRIVILEGE_COOKIE_KEY, user.getIsAdmin() ? "T" : "F");
            privilegeCookie.setMaxAge(-1);
            privilegeCookie.setPath("/");
            response.addCookie(privilegeCookie);
        } catch (Exception e) {
            LOGGER.warn("generate token fail");
            return "login";
        }
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register_submit(@ModelAttribute User user) {

        return "login";
    }
}
