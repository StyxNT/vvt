package com.styxnt.vvtserver.controller;

import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.UserService;
import com.styxnt.vvtserver.utils.CommonResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供用户登录注册等接口的控制器
 *
 * @author StyxNT
 * @date 2021/8/6
 */
@RestController
public class UserController {



    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户注册")
    @PostMapping("/user")
    public CommonResponse register(@RequestBody User user) {
        return userService.register(user);
    }
}
