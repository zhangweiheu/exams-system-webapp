package com.online.exams.system.webapp.controller;

import com.online.exams.system.core.model.User;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhangwei on 16/1/25.
 */
@Controller
@LoginRequired
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView User() {
        ModelAndView view = new ModelAndView();
        view.setViewName("user");
        return view;
    }

    @RequestMapping(value = "/edit/{uid}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable( value = "uid") int uid) {
        ModelAndView view = new ModelAndView();
        view.setViewName("edit_user");
        User user;
        if (uid > 0 && null != (user = userService.findUserByUid(uid))) {
            view.addObject("user",user);
        }
        return view;
    }
}
