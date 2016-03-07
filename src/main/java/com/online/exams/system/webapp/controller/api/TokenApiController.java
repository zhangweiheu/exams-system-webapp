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
 * Created by zhang on 2016/2/24.
 */
@RestController
@RequestMapping("/api/token")
public class TokenApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenApiController.class);

    private static final String SECRET_KEY = "oq0F_m3TP1pXeKTuVUbC25fArMmGHwN9Z-I0Vzx0";
    private static final String ACCESS_KEY = "72bv1OHYgn4I8drbTFOr8e4-lv3Hu329VLnv8c6X";

    @RequestMapping(value = "/geetest", method = RequestMethod.GET)
    public JsonResponse login(HttpServletRequest request) {
        GeetestLib geetestLib = new GeetestLib(GeetestConfig.getCaptchaId(), GeetestConfig.getPrivateKey());
        int gtServerStatus = geetestLib.preProcess();
        request.getSession().setAttribute(geetestLib.gtServerStatusSessionKey,gtServerStatus);
        return JsonResponse.success().add(JSONUtils.parse(geetestLib.getResponseStr()));
    }

    @RequestMapping(value = "/qiniuyun", method = RequestMethod.GET)
    public JsonResponse register_uptoken(HttpServletRequest request) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken("bucket", null, 3600, new StringMap()
                .put("callbackBody", "key=$(key)&hash=$(etag)"));
        LOGGER.info(token);
        return JsonResponse.success(token);
    }
}
