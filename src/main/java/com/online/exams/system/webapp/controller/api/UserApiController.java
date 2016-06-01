package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.model.Question;
import com.online.exams.system.core.model.Tag;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.mybatis.enums.RefTypeEnum;
import com.online.exams.system.core.service.TagService;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.UserHolder;
import com.online.exams.system.webapp.bean.VO.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zhangwei on 16/1/25.
 */
@RestController
@LoginRequired
@RequestMapping("/api/user")
public class UserApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public JsonResponse getUserById(@PathVariable("uid") int uid) {
        if (!checkprivilege(uid)) {
            return JsonResponse.failed();
        }
        User user = userService.findUserByUid(uid);
        user.setPassword(null);
        return JsonResponse.success(user);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JsonResponse updateUser(UserVo userVo) {
        if(!checkprivilege(userVo.getId())){
            return JsonResponse.failed();
        }
        tagService.updateTagList(userVo.getTagList(), userVo.getId(), RefTypeEnum.USER);
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        return JsonResponse.success(userService.updateUser(user));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResponse saveUser(@ModelAttribute UserVo userVo) {
        User user = new User();
        if(null != userService.findUserByName(userVo.getUsername())){
            return JsonResponse.failed("已存在相同用户名");
        }
        if (null != userVo.getTagList()) {
            tagService.saveTagList(userVo.getTagList(), userVo.getId(), RefTypeEnum.USER);
        }
        BeanUtils.copyProperties(userVo, user);
        userService.saveUser(user);
        return JsonResponse.success();
    }

    private Boolean checkprivilege(Integer uid) {
        return uid.equals(UserHolder.getInstance().getUser().getId()) || UserHolder.getInstance().getUser().getType().getValue() > 0;
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

    private static LinkedHashMap<String, String> string2HashMap(String s) {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        String[] s1 = s.split(";");
        for (String tmp : s1) {
            String[] kv = tmp.split(":");
            data.put(kv[0], kv[1]);
        }
        return data;
    }
}
