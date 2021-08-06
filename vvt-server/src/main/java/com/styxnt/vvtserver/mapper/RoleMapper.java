package com.styxnt.vvtserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.styxnt.vvtserver.pojo.Role;

import java.util.List;

/**
 * @Entity com.styxnt.vvtserver.pojo.Role
 * @author StyxNT
 */
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * 根据用户ID查询角色列表
     * @param id
     * @return
     */
    List<Role> getRolesByUserId(Integer id);
}




