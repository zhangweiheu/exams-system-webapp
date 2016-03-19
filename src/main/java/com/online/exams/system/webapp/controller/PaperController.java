package com.online.exams.system.webapp.controller;

import com.online.exams.system.core.bean.MongoPaper;
import com.online.exams.system.core.bean.QuestionMap;
import com.online.exams.system.core.dao.MongoPaperDao;
import com.online.exams.system.core.model.Paper;
import com.online.exams.system.core.service.PaperService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhang on 2016/3/15.
 */
@Controller
@LoginRequired
@RequestMapping("/paper")
public class PaperController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    PaperService paperService;

    @Autowired
    MongoPaperDao mongoPaperDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView paper() {
        ModelAndView view = new ModelAndView();
        view.setViewName("paper");
        int uid = UserHolder.getInstance().getUser().getId();
        view.addObject("uid",uid);
        return view;
    }

//    /**
//     * 获取做过的试卷
//     */
//    @RequestMapping(value = "/{uid}/{pid}", method = RequestMethod.GET)
//    public String redoPaper(@PathVariable("uid") Integer uid, @PathVariable("pid") Integer pid, ModelMap model) {
//        //        User user = UserHolder.getInstance().getUser();
//        //        if (!user.getIsAdmin() || !user.getId().equals(uid)) {
//        //            view.setViewName("login");
//        //            return view;
//        //        }
//        Paper paper = paperService.findPaperById(pid);
//        MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));
//        List<QuestionMap> questionMaps = mongoPaper.getQuestionMapList();
//        for (QuestionMap questionMap : questionMaps) {
//            questionMap.setRight(null);
//            questionMap.setAnswers(null);
//            questionMap.setUpdateAt(null);
//            questionMap.setCreateAt(null);
//        }
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("questions", questionMaps);
//        hashMap.put("pid", paper.getId());
//        model.addAttribute(hashMap);
//        model.addAttribute(uid);
//        return "exam";
//    }

}
