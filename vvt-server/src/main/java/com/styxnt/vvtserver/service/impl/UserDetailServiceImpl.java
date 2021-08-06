package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.styxnt.vvtserver.mapper.UserMapper;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author StyxNT
 * @date 2021/8/6
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username).eq("enabled", true));
        if(user==null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        user.setRoles(roleService.getRolesByUserId(user.getId()));

        return user;
    }
}
