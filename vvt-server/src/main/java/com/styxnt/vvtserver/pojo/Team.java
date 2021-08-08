package com.styxnt.vvtserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 活动小队分组表
 * @TableName t_team
 * @author StyxNT
 */
@TableName(value ="t_team")
@Data
public class Team implements Serializable {
    /**
     * 小队id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 指导老师id
     */
    @TableField(value = "teacher_id")
    private Integer teacherId;

    /**
     * 活动id
     */
    @TableField(value = "activity_id")
    private Integer activityId;

    /**
     *
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate createTime;

    /**
     * 是否活动
     */
    @TableField(value = "active")
    private Integer active;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
