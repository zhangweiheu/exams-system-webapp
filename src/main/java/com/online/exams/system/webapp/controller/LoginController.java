package com.online.exams.system.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangwei on 16/1/13.
 */
@Controller
@RequestMapping("/")
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) {
        LOGGER.info("{} {}请求登陆", username,password);
        return new ModelAndView("index");
    }
}
