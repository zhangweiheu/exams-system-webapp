package com.online.exams.system.webapp.controller;

import com.online.exams.system.core.service.PaperGenerateService;
import com.online.exams.system.core.service.TagService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhang on 2016/3/15.
 */
@Controller
@LoginRequired
@RequestMapping("/exam")
public class ExamController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    PaperGenerateService paperGenerateService;

    @Autowired
    TagService tagService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView paper() {
        ModelAndView view = new ModelAndView();
        view.setViewName("exam");
        int uid = UserHolder.getInstance().getUser().getId();
        view.addObject("uid", uid);
        return view;
    }
}
