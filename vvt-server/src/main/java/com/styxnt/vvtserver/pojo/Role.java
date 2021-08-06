package com.styxnt.vvtserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName t_role
 * @author StyxNT
 */
@TableName(value ="t_role")
@Data
public class Role implements Serializable {
    /**
     * 角色id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色
     */
    @TableField(value = "role")
    private String role;

    /**
     * 角色名
     */
    @TableField(value = "role_name")
    private String roleName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
