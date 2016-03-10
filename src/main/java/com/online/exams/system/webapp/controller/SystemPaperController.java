package com.online.exams.system.webapp.controller;

import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhang on 2016/3/8.
 */
@Controller
@LoginRequired
@RequestMapping("/system/paper")
public class SystemPaperController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemPaperController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView paper() {
        ModelAndView view = new ModelAndView();
        view.setViewName("system_paper");
        return view;
    }

}
