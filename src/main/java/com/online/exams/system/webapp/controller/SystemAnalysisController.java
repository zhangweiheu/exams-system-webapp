package com.online.exams.system.webapp.controller;

import com.online.exams.system.webapp.annotation.AdminOnly;
import com.online.exams.system.webapp.annotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhang on 2016/3/8.
 */
@Controller
@LoginRequired
@AdminOnly
@RequestMapping("/system/analysis")
public class SystemAnalysisController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemAnalysisController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView analysis() {
        ModelAndView view = new ModelAndView();
        view.setViewName("system_analysis");
        return view;
    }

}
