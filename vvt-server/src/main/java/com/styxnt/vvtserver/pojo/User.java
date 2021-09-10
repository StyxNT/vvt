package com.styxnt.vvtserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @TableName t_user
 * @author StyxNT
 */
@TableName(value ="t_user")
@Data
@JsonIgnoreProperties({ "authorities","credentialsNonExpired","accountNonExpired","accountNonLocked"})
public class User implements Serializable ,UserDetails{
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private Object gender;

    /**
     * 用户登录名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户头像
     */
    @TableField(value = "userface")
    private String userface;

    /**
     * 账户是否启用
     *
     * 由于重写了UserDetails中的isEnable方法
     * 这会导致mybatis数据绑定出现错误
     * 所以要防止lombok生成这个字段的get方法
     */
    @TableField(value = "enabled")
    @Getter(AccessLevel.NONE)
    private Boolean enabled=true;

    /**
     * 注册时选择的角色
     */
    @TableField(exist = false)
    private String role;

    /**
     * 账户角色列表
     */
    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 在进行数据库查询的时候，如果涉及到用户的数据查询，则必须要把权限和是否启用全都查出来，否则会报错
     * 为了避免这个问题查询用户的权限，这里直接伪造了一个权限，以此避免在角色表roles为空时的报错问题
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roles==null){
            HashSet<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_NULL"));
            return simpleGrantedAuthorities;
        }else {
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRole()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public void setAuthorities(){
        ;
    }

}
