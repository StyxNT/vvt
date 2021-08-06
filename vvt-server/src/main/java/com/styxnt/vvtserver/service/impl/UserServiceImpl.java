package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.styxnt.vvtserver.mapper.RoleMapper;
import com.styxnt.vvtserver.mapper.UserMapper;
import com.styxnt.vvtserver.mapper.UserRoleMapper;
import com.styxnt.vvtserver.pojo.Role;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.pojo.UserRole;
import com.styxnt.vvtserver.service.UserService;
import com.styxnt.vvtserver.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author StyxNT
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public CommonResponse register(User user) {

        //查询用户想要注册的角色信息
        Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("role", user.getRole()));
        if (null == role) {
            return CommonResponse.error("角色不存在！");
        }
        //加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userMapper.insert(user);

        UserRole userRole=new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());

        if(1==userRoleMapper.insert(userRole)){
            return CommonResponse.success("注册成功");
        }


        return CommonResponse.error("注册失败");
    }
}




