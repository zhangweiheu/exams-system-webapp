package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhang on 2016/2/20.
 */
@RestController
@RequestMapping("/api/common")
public class CommonApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonApiController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/check/username", method = RequestMethod.POST)
    public JsonResponse checkUsername(@RequestParam("username")String username) {
        if(null == userService.findUserByName(username)){
            return JsonResponse.success();
        }
        return JsonResponse.failed();
    }
}
