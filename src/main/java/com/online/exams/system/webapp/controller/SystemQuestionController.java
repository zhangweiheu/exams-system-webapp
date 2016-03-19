package com.online.exams.system.webapp.controller;

import com.online.exams.system.core.enums.RefTypeEnum;
import com.online.exams.system.core.model.Question;
import com.online.exams.system.core.model.Tag;
import com.online.exams.system.core.service.QuestionService;
import com.online.exams.system.core.service.TagService;
import com.online.exams.system.webapp.annotation.AdminOnly;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.VO.QuestionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zhang on 2016/3/8.
 */
@Controller
@LoginRequired
@AdminOnly
@RequestMapping("/system/question")
public class SystemQuestionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemQuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    TagService tagService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView question() {
        ModelAndView view = new ModelAndView();
        view.setViewName("system_question");
        return view;
    }

    @RequestMapping(value = "/edit/{qid}", method = RequestMethod.GET)
    public ModelAndView editQuestion(@PathVariable("qid") int qid) {
        ModelAndView view = new ModelAndView();
        Question question = questionService.findQuestionById(qid);
        if (null != question) {
            QuestionVo questionVo = new QuestionVo();
            BeanUtils.copyProperties(question, questionVo);
            questionVo.setTagList(convertTagList2String(question));
            questionVo.setUpdateAt(null);
            questionVo.setCreateAt(null);
            view.addObject(questionVo);
        }
        view.setViewName("edit_question");
        return view;
    }

    private String convertTagList2String(Question question) {
        Tag tag = new Tag();
        tag.setRefId(question.getId());
        tag.setRefType(RefTypeEnum.QUESTION);
        List<Tag> tagList = tagService.findAllTagByTagAttr(tag);
        StringBuffer stringBuffer = new StringBuffer();
        for (Tag tag1 : tagList) {
            stringBuffer.append(tag1.getTagValue() + ",");
        }
        return stringBuffer.toString();
    }
}
