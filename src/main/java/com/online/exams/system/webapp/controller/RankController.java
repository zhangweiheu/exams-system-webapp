package com.online.exams.system.webapp.controller;

import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhang on 2016/3/15.
 */
@Controller
@LoginRequired
@RequestMapping("/rank")
public class RankController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView rank() {
        ModelAndView view = new ModelAndView();
        view.setViewName("rank");
        int uid = UserHolder.getInstance().getUser().getId();
        view.addObject("uid", uid);
        return view;
    }

}
