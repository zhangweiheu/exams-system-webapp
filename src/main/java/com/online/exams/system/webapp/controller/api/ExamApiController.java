package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.MongoPaper;
import com.online.exams.system.core.bean.QuestionMap;
import com.online.exams.system.core.dao.MongoPaperDao;
import com.online.exams.system.core.enums.RefTypeEnum;
import com.online.exams.system.core.enums.StatusEnum;
import com.online.exams.system.core.enums.TagEnum;
import com.online.exams.system.core.model.Paper;
import com.online.exams.system.core.service.PaperGenerateService;
import com.online.exams.system.core.service.PaperService;
import com.online.exams.system.core.service.TagService;
import com.online.exams.system.core.util.PaperUtil;
import com.online.exams.system.webapp.annotation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhang on 2016/3/8.
 */
@RestController
@LoginRequired
@RequestMapping("/api/system/exam")
public class ExamApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamApiController.class);

    @Autowired
    PaperService paperService;

    @Autowired
    MongoPaperDao mongoPaperDao;

    @Autowired
    PaperGenerateService paperGenerateService;

    @Autowired
    TagService tagService;

    /**
     * 做题
     */
    @RequestMapping(value = "/do/{pid}", method = RequestMethod.PUT)
    public JsonResponse updatePaperById(@RequestParam("answersList") String answersList, @PathVariable("pid") Integer pid) {
        HashMap<Integer, String> newAnswers = convertAnswersListString2AnswersMap(answersList);
        Paper paper = paperService.findPaperById(pid);
        if (paper.getStatus() == StatusEnum.CLOSE || paper.getStatus() == StatusEnum.DELETE) {
            return JsonResponse.failed("试卷不可修改");
        }
        MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));

        List<QuestionMap> questionMaps = mongoPaper.getQuestionMapList();
        for (QuestionMap qMap : questionMaps) {
            if (newAnswers.containsKey(qMap.getId())) {
                qMap.setCurrentAnswer(newAnswers.get(qMap.getId()));
                qMap.setRight(qMap.getAnswers().equals(qMap.getCurrentAnswer()));
            }
        }
        mongoPaper.setQuestionMapList(questionMaps);

        paper.setScore(PaperUtil.calautePaperScore(questionMaps));
        paper.setTotalRight(PaperUtil.calautePaperTotalSuccess(questionMaps));
        paperService.updatePaper(paper);

        mongoPaperDao.addPaper(mongoPaper);
        return JsonResponse.success();
    }

    @RequestMapping(value = "/close/{uid}/{pid}", method = RequestMethod.PUT)
    public JsonResponse closeExam(@PathVariable("uid") int uid, @PathVariable("pid") int pid) {
        Paper paper = new Paper();
        paper.setId(pid);
        paper.setUserId(uid);
        paper.setStatus(StatusEnum.CLOSE);
        return JsonResponse.success(paperService.updatePaper(paper));
    }

    private HashMap<Integer, String> convertAnswersListString2AnswersMap(String answersList) {
        HashMap<Integer, String> answers = new HashMap<>();
        answersList = answersList.subSequence(1, answersList.length() - 1).toString();
        String[] answerArray = answersList.split(",");
        for (String s : answerArray) {
            String[] tem = s.split(":");
            answers.put(Integer.parseInt(tem[0]), tem[1]);
        }
        return answers;
    }

    @RequestMapping(value = "/programing", method = RequestMethod.POST)
    public JsonResponse programingCheck(@RequestParam("pid") int pid, @RequestParam("qid") int qid, @RequestParam("text") String text) {
        //// TODO: 2016/3/18 处理编程题

        return JsonResponse.success();
    }

    /**
     * 新生成试卷
     */
    @RequestMapping(value = "/generate/{uid}", method = RequestMethod.POST)
    public JsonResponse doPaper(@PathVariable("uid") Integer uid, @RequestParam("questionTagList") String questionTagList, @RequestParam("paperType") String paperType, ModelMap model) {
        Paper paper = paperService.findDoingPaperBy(uid);
        HashMap<String, Object> hashMap = new HashMap<>();
        if (null != paper) {
            MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));
            hashMap.put("questions", mongoPaper.getQuestionMapList());
            hashMap.put("pid", paper.getId());
        } else {
            List<TagEnum> tagEnumList = convertTagLisString2TagList(questionTagList);
            switch (paperType) {
                case "SINGLE_SELECTION":
                    hashMap = paperGenerateService.generateSingleSelection(tagEnumList, uid);
                    break;
                case "MULTI_SELECTION":
                    hashMap = paperGenerateService.generateMultiSelection(tagEnumList, uid);
                    break;
                case "PROGRAMMING_QUESTION":
                    hashMap = paperGenerateService.generateProgrammingQuestion(tagEnumList, uid);
                    break;
                case "SINGLE_AND_MULTI":
                    hashMap = paperGenerateService.generateSingleMultiSelection(tagEnumList, uid);
                    break;
                case "SINGLE_AND_PROGRAMMING":
                    hashMap = paperGenerateService.generateSingleProgrammingQuestion(tagEnumList, uid);
                    break;
                case "MULTI_AND_PROGRAMMING":
                    hashMap = paperGenerateService.generateMultiProgrammingSelection(tagEnumList, uid);
                    break;
                case "SINGLE_AND_MULTI_PROGRAMMING":
                    hashMap = paperGenerateService.generateSingleMultiProgrammingQuestion(tagEnumList, uid);
                    break;
                default:
                    hashMap = paperGenerateService.generateSingleMultiProgrammingQuestion(tagEnumList, uid);
                    break;
            }
            tagService.saveTagList(questionTagList, (Integer) hashMap.get("pid"), RefTypeEnum.PAPER);
        }

        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.put("uid", uid);
        jsonResponse.put("questions", hashMap.get("questions"));
        jsonResponse.put("pid", hashMap.get("pid"));

        return jsonResponse;
    }

    private List<TagEnum> convertTagLisString2TagList(String enumList) {
        List<TagEnum> tagEnumList = new ArrayList<>();
        if (StringUtils.isEmpty(enumList)) {
            return tagEnumList;
        }
        String[] chars = enumList.split(",");
        for (String c : chars) {
            tagEnumList.add(TagEnum.valueOf(c));
        }
        return tagEnumList;
    }

}
