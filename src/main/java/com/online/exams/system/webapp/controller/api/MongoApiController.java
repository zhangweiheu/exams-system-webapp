package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.MongoPaper;
import com.online.exams.system.core.dao.MongoPaperDao;
import com.online.exams.system.webapp.annotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created by zhangwei on 16/1/25.
 */
@RestController
@LoginRequired
@RequestMapping("/api/mongo/")
public class MongoApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoApiController.class);

    @Autowired
    private MongoPaperDao mongoPaperDao;

    @RequestMapping(value = "/paper", method = RequestMethod.GET)
    public JsonResponse getMongoPaperList() {
        MongoPaper mongoPaper = new MongoPaper();
        HashMap<Integer,String> hashMap =new HashMap<>();
        hashMap.put(8,"abcd");
        return JsonResponse.success(mongoPaperDao.addPaper(mongoPaper));
    }

    @RequestMapping(value = "/paper/{mpid}", method = RequestMethod.GET)
    public JsonResponse getMongoPaperById(@PathVariable("mpid") String mpid) {
        MongoPaper mongoPaper = new MongoPaper();
        HashMap<Integer,String> hashMap =new HashMap<>();
        hashMap.put(8,"abcd");
        return JsonResponse.success(mongoPaperDao.findMongoPaperById(Long.parseLong(mpid)));
    }


}
