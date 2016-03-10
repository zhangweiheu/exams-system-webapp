package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.webapp.annotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhang on 2016/3/8.
 */
@Controller
@LoginRequired
@RequestMapping("/api/system/question")
public class QuestionApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionApiController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public JsonResponse getQuestionList() {
        return null;
    }

}
