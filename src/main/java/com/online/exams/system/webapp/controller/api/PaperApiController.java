package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.MongoPaper;
import com.online.exams.system.core.bean.Page;
import com.online.exams.system.core.bean.QuestionMap;
import com.online.exams.system.core.dao.MongoPaperDao;
import com.online.exams.system.core.model.Paper;
import com.online.exams.system.core.model.Tag;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.mybatis.enums.RefTypeEnum;
import com.online.exams.system.core.mybatis.enums.StatusEnum;
import com.online.exams.system.core.service.PaperGenerateService;
import com.online.exams.system.core.service.PaperService;
import com.online.exams.system.core.service.QuestionService;
import com.online.exams.system.core.service.TagService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.UserHolder;
import com.online.exams.system.webapp.bean.VO.PaperVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/3/8.
 */
@RestController
@LoginRequired
@RequestMapping("/api/system/paper")
public class PaperApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaperApiController.class);

    @Autowired
    PaperService paperService;

    @Autowired
    MongoPaperDao mongoPaperDao;

    @Autowired
    QuestionService questionService;


    @Autowired
    TagService tagService;

    @Autowired
    PaperGenerateService paperGenerateService;

    /**
     * 获取试卷列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse getPaperList(@ModelAttribute Page page) {
        List<Paper> paperList;
        User user = UserHolder.getInstance().getUser();
        if (user.getType().getValue() > 0) {
            paperList = paperService.listAllPaper(page.getOffset(), page.getPageSize());
        } else {
            paperList = paperService.listAllPaper(page.getOffset(), page.getPageSize(), user.getId());
        }
        List<PaperVo> paperVos = new ArrayList<>();
        for (Paper paper : paperList) {
            PaperVo paperVo = new PaperVo();
            BeanUtils.copyProperties(paper, paperVo);
            paperVo.setProperties("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(paper.getCreateAt()));
            paperVo.setCreateAt(null);
            paperVo.setUpdateAt(null);
            paperVo.setExam(paper.getIsExam());
            paperVo.setTagList(convertTagList2String(paper.getId()));
            paperVos.add(paperVo);
        }
        page.setData(paperVos);
        page.setTotalCount(paperService.countAllPapersByAttr(null));
        return JsonResponse.success(page);
    }

    /**
     * 软删除试卷
     */
    @RequestMapping(value = "/{pid}", method = RequestMethod.DELETE)
    public JsonResponse deletePaperById(@PathVariable("pid") int pid) {
        return JsonResponse.success(paperService.deletePaperById(pid));
    }

    /**
     * 设置paper不可修改:提交试卷/管理员设置
     */
    @RequestMapping(value = "/{uid}/{pid}", method = RequestMethod.PUT)
    public JsonResponse setPaperUnUpdate(@PathVariable("uid") int uid, @PathVariable("pid") int pid) {
        if (uid != 0 && uid == UserHolder.getInstance().getUser().getId()) {} else if (uid == 0
                && UserHolder.getInstance().getUser().getType().getValue() > 0) {} else {
            return JsonResponse.failed("操作无无权限");
        }
        Paper paper = paperService.findPaperById(pid);
        if (paper.getStatus() == StatusEnum.DELETE) {
            return JsonResponse.failed("试卷已删除，不可修改");
        }
        if (paper.getStatus() == StatusEnum.CLOSE) {
            paper.setStatus(StatusEnum.NORMAL);
            paperService.updatePaper(paper);
            return JsonResponse.success("试卷打开成功");
        }
        paper.setStatus(StatusEnum.CLOSE);
        paperService.updatePaper(paper);
        return JsonResponse.success("试卷关闭成功");
    }

    /**
     * //     * 获取做过的试卷，不允许修改
     * //
     */
    @RequestMapping(value = "/{uid}/{pid}", method = RequestMethod.GET)
    public JsonResponse redoPaper(@PathVariable("uid") Integer uid, @PathVariable("pid") Integer pid, ModelMap model) {
        //        User user = UserHolder.getInstance().getUser();
        //        if (!user.getIsAdmin() || !user.getId().equals(uid)) {
        //            view.setViewName("login");
        //            return view;
        //        }
        //// TODO: 2016/3/18 数据带不过去
        Paper paper = paperService.findPaperById(pid);
        MongoPaper mongoPaper = mongoPaperDao.findMongoPaperById(Integer.toUnsignedLong(paper.getMongoPaperId()));
        List<QuestionMap> questionMaps = mongoPaper.getQuestionMapList();
        for (QuestionMap questionMap : questionMaps) {
            questionMap.setRight(null);
            questionMap.setAnswers(null);
            questionMap.setUpdateAt(null);
            questionMap.setCreateAt(null);
        }
        JsonResponse jsonResponse = JsonResponse.success();
        jsonResponse.put("uid", uid);
        jsonResponse.put("questions", questionMaps);
        jsonResponse.put("pid", paper.getId());
        return jsonResponse;
    }

    private String convertTagList2String(int pid) {
        Tag tag = new Tag();
        tag.setRefId(pid);
        tag.setRefType(RefTypeEnum.PAPER);
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
}
