package com.online.exams.system.webapp.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.Page;
import com.online.exams.system.core.bean.TestCase;
import com.online.exams.system.core.dao.MongoTestCaseDao;
import com.online.exams.system.core.enums.QuestionTypeEnum;
import com.online.exams.system.core.enums.RefTypeEnum;
import com.online.exams.system.core.enums.StatusEnum;
import com.online.exams.system.core.model.Question;
import com.online.exams.system.core.model.Tag;
import com.online.exams.system.core.service.QuestionService;
import com.online.exams.system.core.service.TagService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.VO.QuestionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhang on 2016/3/8.
 */
@RestController
@LoginRequired
@RequestMapping("/api/system/question")
public class QuestionApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionApiController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    TagService tagService;

    @Autowired
    MongoTestCaseDao mongoTestCaseDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse getQuestionList(@ModelAttribute Page page) {
        List<Question> questionList = questionService.listAllQuestion(page.getOffset(), page.getPageSize());
        List<QuestionVo> questionVoList = new ArrayList<>();
        for (Question question : questionList) {
            QuestionVo questionVo = new QuestionVo();
            BeanUtils.copyProperties(question, questionVo);
            questionVo.setTagList(convertTagList2String(question));
            questionVo.setProperties("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(question.getCreateAt()));
            questionVo.setCreateAt(null);
            questionVo.setUpdateAt(null);
            questionVoList.add(questionVo);
        }
        page.setTotalCount(questionService.getTotalCount(new Question()));
        page.setData(questionVoList);
        return JsonResponse.success(page);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JsonResponse updateQuestuion(@RequestParam("id") Integer id, @RequestParam("title") String title, @RequestParam("options") String options, @RequestParam("answers") String answers, @RequestParam("difficulty") Integer difficulty, @RequestParam("priority") Integer priority, @RequestParam("tagList") String tagList, @RequestParam("status") StatusEnum status, @RequestParam("questionType") QuestionTypeEnum questionType) {
        Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        question.setOptions(options);
        question.setAnswers(answers);
        question.setDifficulty(difficulty);
        question.setPriority(priority);
        question.setQuestionType(questionType);
        question.setStatus(status);
        question.setUpdateAt(new Date());
        questionService.updateQuestion(question);
        tagService.updateTagList(tagList, id, RefTypeEnum.QUESTION);
        return JsonResponse.success();
    }

    @RequestMapping(value = "/programing", method = RequestMethod.PUT)
    public JsonResponse updateProgramingQuestuion(@RequestParam("id") Integer id, @RequestParam("title") String title, @RequestParam("options") String options, @RequestParam("answers") String answers, @RequestParam("difficulty") Integer difficulty, @RequestParam("priority") Integer priority, @RequestParam("tagList") String tagList, @RequestParam("status") StatusEnum status, @RequestParam("questionType") QuestionTypeEnum questionType) {
        Question question = questionService.findQuestionById(id);
        TestCase testCase = new TestCase();
        testCase.setId(Long.parseLong(question.getAnswers()));
        testCase.setKeyValue(string2HashMap(question.getAnswers()));

        long qtid = mongoTestCaseDao.updateTestCaseByTCID(testCase);

        question.setAnswers(String.valueOf(qtid));
        question.setTitle(title);
        question.setOptions(options);
        question.setDifficulty(difficulty);
        question.setPriority(priority);
        question.setQuestionType(questionType);
        question.setStatus(status);
        question.setUpdateAt(new Date());

        questionService.updateQuestion(question);
        tagService.updateTagList(tagList, id, RefTypeEnum.QUESTION);
        return JsonResponse.success();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResponse saveQuestion(@ModelAttribute QuestionVo questionVo) {
        Question question = new Question();
        BeanUtils.copyProperties(questionVo, question);
        question.setCreateAt(new Date());
        question.setCreateAt(new Date());
        if (null != questionService.findCommonQuestion(question)) {
            return JsonResponse.failed("已存在相同题目");
        }
        questionService.saveQuestion(question);
        if (null != questionVo.getTagList()) {
            tagService.saveTagList(questionVo.getTagList(), question.getId(), RefTypeEnum.QUESTION);
        }
        return JsonResponse.success();
    }

    @RequestMapping(value = "/programing", method = RequestMethod.POST)
    public JsonResponse saveProgramingQuestion(@ModelAttribute QuestionVo questionVo) {

        Question question = new Question();
        BeanUtils.copyProperties(questionVo, question);
        question.setCreateAt(new Date());
        question.setCreateAt(new Date());
        if (null != questionService.findCommonQuestion(question)) {
            return JsonResponse.failed("已存在相同题目");
        }

        TestCase testCase = new TestCase();
        testCase.setCreateAt(new Date());
        testCase.setUpdateAt(new Date());
        testCase.setKeyValue(string2HashMap(question.getAnswers()));

        long qtid = mongoTestCaseDao.addTestCase(testCase);

        question.setAnswers(String.valueOf(qtid));
        questionService.saveQuestion(question);
        if (null != questionVo.getTagList()) {
            tagService.saveTagList(questionVo.getTagList(), question.getId(), RefTypeEnum.QUESTION);
        }
        return JsonResponse.success();
    }

    @RequestMapping(value = "/{qid}", method = RequestMethod.DELETE)
    public JsonResponse deleteQuestion(@PathVariable("qid") int qid) {
        questionService.deleteQuestionById(qid);
        Tag tag = new Tag();
        tag.setRefId(qid);
        tag.setRefType(RefTypeEnum.QUESTION);
        tagService.deleteTagByTagAttr(tag);
        return JsonResponse.success();
    }

    private String convertTagList2String(Question question) {
        Tag tag = new Tag();
        tag.setRefId(question.getId());
        tag.setRefType(RefTypeEnum.QUESTION);
        List<Tag> tagList = tagService.findAllTagByTagAttr(tag);
        if (CollectionUtils.isEmpty(tagList)) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Tag tag1 : tagList) {
            stringBuffer.append(tag1.getTagValue() + ",");
        }
        return stringBuffer.subSequence(0, stringBuffer.length() - 1).toString();
    }

    private static HashMap<String, String> string2HashMap(String s) {
        HashMap<String, String> data = new HashMap<>();
        String[] s1 = s.split(";");
        for (String tmp : s1) {
            String[] kv = tmp.split(":");
            data.put(kv[0], kv[1]);
        }
        return data;
    }
}
