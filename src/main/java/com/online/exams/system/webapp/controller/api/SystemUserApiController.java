package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.Page;
import com.online.exams.system.core.mybatis.enums.RefTypeEnum;
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
    private UserService userService;

    @Autowired
    private TagService tagService;

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

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JsonResponse updateUser(UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        userService.updateUser(user);
        tagService.updateTagList(userVo.getTagList(), userVo.getId(), RefTypeEnum.USER);
        return JsonResponse.success();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
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
