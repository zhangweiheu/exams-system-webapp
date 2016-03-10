package com.online.exams.system.webapp.controller;

import com.online.exams.system.webapp.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhang on 2016/3/7.
 */
@Controller
@RequestMapping("/index")
@LoginRequired
public class IndexController {
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String Index(){
        return "index";
    }
}
