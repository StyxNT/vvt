package com.styxnt.vvtserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.utils.CommonResponse;

/**
 *@author StyxNT
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param user
     * @return
     */
    CommonResponse register(User user);
}
