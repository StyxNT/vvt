package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.styxnt.vvtserver.mapper.RoleMapper;
import com.styxnt.vvtserver.mapper.UserMapper;
import com.styxnt.vvtserver.mapper.UserRoleMapper;
import com.styxnt.vvtserver.pojo.LoginParam;
import com.styxnt.vvtserver.pojo.Role;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.pojo.UserRole;
import com.styxnt.vvtserver.service.UserService;
import com.styxnt.vvtserver.utils.CommonResponse;
import com.styxnt.vvtserver.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

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

    /**
     * 用户登录
     * @param loginUser
     * @return
     */
    @Override
    public CommonResponse login(LoginParam loginUser) {

        UserDetails userDetails;

        try {
            userDetails=userDetailsService.loadUserByUsername(loginUser.getUsername());
        }catch (UsernameNotFoundException e){
            return CommonResponse.error("用户不存在！");
        }

        if(userDetails==null||!passwordEncoder.matches(loginUser.getPassword(),userDetails.getPassword())){
            return CommonResponse.error("用户名或密码错误！");
        }
        if(!userDetails.isEnabled()){
            return CommonResponse.error("账户被禁用，请联系管理员！");
        }

        //        更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                userDetails,null,userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //        生成token
        String token = jwtTokenUtils.generateToken(userDetails);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);

        return CommonResponse.success("登录成功",tokenMap);
    }
}




