package com.online.exams.system.webapp.controller;

import com.online.exams.system.core.model.User;
import com.online.exams.system.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhang on 2016/2/20.
 */
@Controller
@RequestMapping("/register")
public class RegisterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ModelAndView registerForm(@ModelAttribute User user, ModelAndView modelAndView) {
        if (null == userService.findUserByName(user.getUsername())) {
            userService.saveUser(user);
            modelAndView.getModelMap().addAttribute("msg","您已经成功注册，请登录");
            modelAndView.setViewName("login");
        } else {
            modelAndView.addObject("user",user);
            modelAndView.setViewName("register");
        }
        return modelAndView;
    }


}
