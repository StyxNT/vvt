package com.styxnt.vvtserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单表
 * @TableName t_menu
 * @author StyxNT
 */
@TableName(value ="t_menu")
@Data
public class Menu implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 请求的地址
     */
    @TableField(value = "url")
    private String url;

    /**
     * 菜单名字
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 是否启用
     */
    @TableField(value = "enabled")
    private Integer enabled;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
