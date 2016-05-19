package com.online.exams.system.webapp.controller;

import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.annotation.NotNeedLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhangwei on 16/1/13.
 */
@Controller
@RequestMapping("")
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @NotNeedLogin
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        return view;
    }

    @NotNeedLogin
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login2() {
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        return view;
    }

    @NotNeedLogin
    @RequestMapping(value = "/skip", method = RequestMethod.GET)
    public ModelAndView skip() {
        ModelAndView view = new ModelAndView();
        view.setViewName("skip");
        return view;
    }

    @LoginRequired
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView view = new ModelAndView();
        view.setViewName("index");
        return view;
    }
}
