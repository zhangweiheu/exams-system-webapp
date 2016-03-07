package com.online.exams.system.webapp.controller.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.webapp.geetest.GeetestConfig;
import com.online.exams.system.webapp.geetest.GeetestLib;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhang on 2016/2/20.
 */
@RestController
@RequestMapping("/api/register")
public class RegisterApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterApiController.class);
}
