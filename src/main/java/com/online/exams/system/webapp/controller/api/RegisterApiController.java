package com.online.exams.system.webapp.controller.api;

import com.online.exams.system.core.bean.JsonResponse;
import com.online.exams.system.core.model.User;
import com.online.exams.system.core.mybatis.enums.UserStatusEnum;
import com.online.exams.system.core.mybatis.enums.UserTypeEnum;
import com.online.exams.system.core.service.UserService;
import com.online.exams.system.webapp.bean.VO.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zhang on 2016/2/20.
 */
@RestController
@RequestMapping("/api/register")
public class RegisterApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterApiController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResponse registerForm(@ModelAttribute UserVo userVo, ModelAndView modelAndView) {
        if (null == userService.findUserByName(userVo.getUsername()) && userVo.getPassword().equals(userVo.getPassword2())) {
            User user = new User();
            BeanUtils.copyProperties(userVo,user);
            user.setStatus(UserStatusEnum.AUDITING);
            user.setType(UserTypeEnum.COMMON);
            userService.saveUser(user);
            return JsonResponse.success();
        }
        return JsonResponse.failed("注册失败！").put("user",userVo);
    }
}
