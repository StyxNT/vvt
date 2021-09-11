package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.styxnt.vvtserver.constants.RedisConstants;
import com.styxnt.vvtserver.mapper.UserMapper;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //尝试从redis中获取用户信息
        User user = (User) redisTemplate.opsForValue().get(RedisConstants.LOGIN_USER+username);
        //redis中没有就从数据库中查询
        if(user==null){
            user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username).eq("enabled", true));
        } else {
            return user;
        }
        //如果未从数据库中找到用户信息就抛出异常，否则查询用户的角色并存入redis
        if(user==null){
            throw new UsernameNotFoundException("用户名不存在");
        }else{

            user.setRoles(roleService.getRolesByUserId(user.getId()));
            redisTemplate.opsForValue().set(RedisConstants.LOGIN_USER+username,user,30, TimeUnit.MINUTES);
        }

        return user;
    }
}
