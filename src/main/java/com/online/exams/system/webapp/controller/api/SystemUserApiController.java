package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.Page;
import com.online.exams.system.core.enums.RefTypeEnum;
import com.online.exams.system.core.model.Tag;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.service.TagService;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.AdminOnly;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.VO.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhang on 2016/3/8.
 */
@RestController
@LoginRequired
@AdminOnly
@RequestMapping("/api/system/user")
public class SystemUserApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUserApiController.class);

    @Autowired
    UserService userService;

    @Autowired
    TagService tagService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse getuserList(@ModelAttribute Page page) {
        List<User> userList = userService.listAllUser(page.getOffset(), page.getPageSize());
        List<UserVo> userVoList = new ArrayList<>();
        //日期处理
        for (User user : userList) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVo.setProperties("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getCreateAt()));
            userVo.setUpdateAt(null);
            userVo.setPassword(null);
            userVo.setCreateAt(null);
            userVo.setAdmin(user.getIsAdmin());
            userVo.setDelete(user.getIsDelete());
            userVo.setTagList(convertTagList2String(user));

            userVoList.add(userVo);
        }
        page.setTotalCount(userService.getTotalCount());
        page.setData(userVoList);
        return JsonResponse.success(page);
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.DELETE)
    public JsonResponse deleteUserById(@PathVariable("uid") int uid) {
        userService.deleteUserByUid(uid);
        return JsonResponse.success();
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public JsonResponse updateUser(@PathVariable("id") Integer id, @PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("isAdmin") Boolean isAdmin, @PathVariable("isDelete") Boolean isDelete, @PathVariable("tagList") String tagList, @PathVariable("intro") String intro, @PathVariable("email") String email, @PathVariable("phone") String phone, @PathVariable("wechat") String wechat, @PathVariable("totalScore") Integer totalScore, @PathVariable("totalDone") Integer totalDone, @PathVariable("averageScore") Integer averageScore) {

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setIsDelete(isDelete);
        user.setIsAdmin(isAdmin);
        user.setTotalDone(totalDone);
        user.setTotalScore(totalScore);
        user.setAverageScore(averageScore);
        user.setEmail(email);
        user.setPhone(phone);
        user.setWechat(wechat);
        user.setIntro(intro);
        user.setUpdateAt(new Date());
        userService.updateUser(user);
        tagService.updateTagList(tagList, id, RefTypeEnum.USER);
        return JsonResponse.success();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public JsonResponse saveUser(@ModelAttribute UserVo userVo) {
        User user = new User();
        if (null != userService.findUserByName(userVo.getUsername())) {
            return JsonResponse.failed("已存在相同用户名");
        }
        BeanUtils.copyProperties(userVo, user);
        user.setCreateAt(new Date());
        user.setUpdateAt(new Date());
        userService.saveUser(user);
        if (StringUtils.isNotEmpty(userVo.getTagList())) {
            tagService.updateTagList(userVo.getTagList(), userVo.getId(), RefTypeEnum.USER);
        }
        return JsonResponse.success();
    }

    private String convertTagList2String(User user) {
        Tag tag = new Tag();
        tag.setRefId(user.getId());
        tag.setRefType(RefTypeEnum.USER);
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
