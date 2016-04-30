package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.Page;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.service.DataService;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.AdminOnly;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhang on 2016/3/8.
 */
@RestController
@LoginRequired
@RequestMapping("/api/analysis")
public class AnalysisApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisApiController.class);

    @Autowired
    DataService dataService;

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse getRankList(Page page) {
        List<User> userList = dataService.getAllTotalScoreOrder(page.getOffset(), page.getPageSize());
        page.setData(userList);
        page.setTotalCount(userService.getTotalCount());
        return JsonResponse.success(page);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public JsonResponse analysis() {
        int uid = UserHolder.getInstance().getUser().getId();
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.put("userPaper", dataService.getUserPaperTendency(uid));
        jsonResponse.put("questionType", dataService.getAnalysisDataByQuestionType(uid));

        return jsonResponse;
    }

    @AdminOnly
    @RequestMapping(value = "/system", method = RequestMethod.GET)
    public JsonResponse systemAnalysis() {
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.put("question", dataService.getAnalysisDataByQuestionType());
        return jsonResponse;
    }

}
