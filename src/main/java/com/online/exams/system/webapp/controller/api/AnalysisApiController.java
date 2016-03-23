package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.webapp.annotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhang on 2016/3/8.
 */
@RestController
@LoginRequired
@RequestMapping("/api/analysis")
public class AnalysisApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisApiController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public JsonResponse analysis() {
        return JsonResponse.success();
    }

}
