package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.annotation.LoginRequired;
import com.online.exams.system.webapp.bean.UserHolder;
import com.online.exams.system.webapp.bean.VO.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    public JsonResponse getUserById(@PathVariable("uid") int uid) {
        if (!checkprivilege(uid)) {
            return JsonResponse.failed();
        }
        User user = userService.findUserByUid(uid);
        user.setPassword(null);
        return JsonResponse.success(user);
    }



    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public JsonResponse updateUser(@ModelAttribute User user) {
        if(!checkprivilege(user.getId())){
            return JsonResponse.failed();
        }
        return JsonResponse.success(userService.updateUser(user));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public JsonResponse saveUser(@ModelAttribute UserVo userVo) {
        User user = new User();
        if(null != userService.findUserByName(userVo.getUsername())){
            return JsonResponse.failed("已存在相同用户名");
        }
        BeanUtils.copyProperties(userVo, user);
        userService.saveUser(user);
        return JsonResponse.success();
    }

    private Boolean checkprivilege(Integer uid) {
        return uid.equals(UserHolder.getInstance().getUser().getId()) || UserHolder.getInstance().getUser().getIsAdmin();
    }
}
