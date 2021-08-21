package com.styxnt.vvtserver.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName t_team_member
 */
@TableName(value ="t_team_member")
@Data
public class TeamMember implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小队id
     */
    @TableField(value = "team_id")
    private Integer teamId;

    /**
     * 成员id
     */
    @TableField(value = "member_id")
    private Integer memberId;

    /**
     * 成员成绩
     */
    @TableField(value = "score")
    private Double score;

    /**
     * 成员评价
     */
    @TableField(value = "comment")
    private String comment;

    /**
     * 成员信息
     */
    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
