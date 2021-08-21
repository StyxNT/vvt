package com.styxnt.vvtserver.controller;

import com.styxnt.vvtserver.pojo.LoginParam;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.UserService;
import com.styxnt.vvtserver.utils.CommonResponse;
import com.styxnt.vvtserver.utils.SecurityContextUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PostMapping("/register")
    public CommonResponse register(@RequestBody User user) {
        return userService.register(user);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public CommonResponse login(@RequestBody LoginParam user){
        return userService.login(user);
    }


    @ApiOperation(value = "退出登录")
    @GetMapping("/logout")
    public CommonResponse logout(){
        SecurityContextHolder.clearContext();
        return CommonResponse.success("退出登录成功");
    }

    @ApiOperation(value = "获取当前登录用户的信息")
    @GetMapping("/user/info")
    public User getUserInfo(){
        return SecurityContextUtil.getContextUser();
    }
}
