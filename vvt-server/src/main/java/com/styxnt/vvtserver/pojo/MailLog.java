package com.styxnt.vvtserver.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @TableName t_mail_log
 */
@TableName(value ="t_mail_log")
@Data
public class MailLog implements Serializable {
    /**
     *
     */
    @TableId(value = "msgId")
    private String msgId;

    /**
     *
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     *
     */
    @TableField(value = "team_id")
    private Integer teamId;

    /**
     *
     */
    @TableField(value = "status")
    private Integer status;

    /**
     *
     */
    @TableField(value = "routeKey")
    private String routeKey;

    /**
     *
     */
    @TableField(value = "exchange")
    private String exchange;

    /**
     *
     */
    @TableField(value = "count")
    private Integer count;

    /**
     *
     */
    @TableField(value = "tryTime")
    private LocalDateTime tryTime;

    /**
     *
     */
    @TableField(value = "createTime")
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(value = "updateTime")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
