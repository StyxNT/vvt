package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.styxnt.vvtserver.mapper.RoleMapper;
import com.styxnt.vvtserver.pojo.Role;
import com.styxnt.vvtserver.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *@author StyxNT
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据用户ID查询角色列表
     * @param id
     * @return
     */
    @Override
    public List<Role> getRolesByUserId(Integer id) {

        return roleMapper.getRolesByUserId(id);
    }
}




