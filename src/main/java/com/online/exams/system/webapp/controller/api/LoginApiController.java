package com.online.exams.system.webapp.controller.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.webapp.geetest.GeetestConfig;
import com.online.exams.system.webapp.geetest.GeetestLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhang on 2016/2/20.
 */
@RestController
@RequestMapping("/api")
public class LoginApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginApiController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JsonResponse login(HttpServletRequest request) {
        GeetestLib geetestLib = new GeetestLib(GeetestConfig.getCaptchaId(), GeetestConfig.getPrivateKey());
        int gtServerStatus = geetestLib.preProcess();
        request.getSession().setAttribute(geetestLib.gtServerStatusSessionKey,gtServerStatus);
        return JsonResponse.success().add(JSONUtils.parse(geetestLib.getResponseStr()));
    }
}
