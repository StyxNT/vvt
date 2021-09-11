package com.styxnt.vvtserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate createTime;

    /**
     * 是否活动
     */
    @TableField(value = "active")
    private Integer active;

    /**
     * 负责老师
     */
    @TableField(exist = false)
    private User teacher;

    /**
     * 活动
     */
    @TableField(exist = false)
    private Activity activity;

    /**
     * 小组成员
     */
    @TableField(exist = false)
    private List<User> members;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
