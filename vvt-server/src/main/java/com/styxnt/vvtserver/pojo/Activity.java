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

/**
 * 志愿活动表
 * @TableName t_activity
 * @author StyxNT
 */
@TableName(value ="t_activity")
@Data
public class Activity implements Serializable {
    /**
     * 活动ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 活动名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 活动创建者id
     */
    @TableField(value = "creator_id")
    private Integer creatorId;

    /**
     * 活动描述
     */
    @TableField(value = "activity_description")
    private String activityDescription;

    /**
     * 活动创建日期
     */
    @TableField(value = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate createDate;

    /**
     * 活动开始时间
     */
    @TableField(value = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    /**
     * 活动结束时间
     */
    @TableField(value = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    /**
     * 最多参与人数
     */
    @TableField(value = "max_participant")
    private Integer maxParticipant;

    /**
     * 是否启用
     */
    @TableField(value = "enabled")
    private Integer enabled;

    /**
     * 是否活动中
     */
    @TableField(value = "active")
    private Integer active;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
