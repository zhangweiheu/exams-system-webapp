package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.webapp.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 36kr on 16/1/25.
 */
@RestController
public class UserApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public String index() {
        LOGGER.info(" API进来了 ");
        return "Greetings from Spring Boot!";
    }
}
