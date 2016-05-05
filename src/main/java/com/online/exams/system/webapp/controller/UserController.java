package com.online.exams.system.webapp.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by zhangwei on 16/1/25.
 */
@Controller
@LoginRequired
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    TagService tagService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView User() {
        ModelAndView view = new ModelAndView();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(UserHolder.getInstance().getUser(), userVo);
        view.addObject(userVo);
        view.setViewName("user");
        return view;
    }

    @RequestMapping(value = "/edit/{uid}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable(value = "uid") int uid) {
        ModelAndView view = new ModelAndView();
        view.setViewName("edit_user");
        User user;
        if (uid > 0 && null != (user = userService.findUserByUid(uid))) {
            if (null != user) {
                UserVo userVo = new UserVo();
                BeanUtils.copyProperties(user, userVo);
                userVo.setUpdateAt(null);
                userVo.setCreateAt(null);
                userVo.setTagList(convertTagList2String(user));
                view.addObject(userVo);
            }
        }
        return view;
    }

    private String convertTagList2String(User user) {
        Tag tag = new Tag();
        tag.setRefId(user.getId());
        tag.setRefType(RefTypeEnum.USER);
        List<Tag> tagList = tagService.findAllTagByTagAttr(tag);
        if (CollectionUtils.isEmpty(tagList)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Tag tag1 : tagList) {
            stringBuffer.append(tag1.getTagValue() + ",");
        }
        return stringBuffer.subSequence(0, stringBuffer.length() - 1).toString();
    }
}
