package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.bean.Page;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.VO.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResponse getuserList(@ModelAttribute Page page) {
        List<User> userList = userService.listAllUser(page.getOffset(),page.getPageSize());
        List<UserVo> userVoList = new ArrayList<>();
        //日期处理
        for (User user : userList) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user,userVo);
            userVo.setProperties("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getCreateAt()));
            userVo.setUpdateAt(null);
            userVo.setPassword(null);
            userVoList.add(userVo);
        }
        page.setTotalCount(userService.getTotalCount());
        page.setData(userVoList);
        return JsonResponse.success(page);
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public JsonResponse getUserById(@PathVariable("uid") int uid) {
        User user = userService.findUserByUid(uid);
        user.setPassword("");
        return JsonResponse.success(user);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public JsonResponse updateUser(@ModelAttribute User user) {
        return JsonResponse.success(userService.updateUser(user));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public JsonResponse saveUser(@ModelAttribute User user) {
        return JsonResponse.success(userService.saveUser(user));
    }
}
