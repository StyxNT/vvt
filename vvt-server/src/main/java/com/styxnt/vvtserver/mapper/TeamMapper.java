package com.styxnt.vvtserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.styxnt.vvtserver.pojo.Team;

import java.util.List;

/**
 * @Entity com.styxnt.vvtserver.pojo.Team
 * @author StyxNT
 */
public interface TeamMapper extends BaseMapper<Team> {

    /**
     * 查询小队信息
     * @param creatorId
     * @return
     */
    List<Team> getAllTeams(Integer creatorId, String keyword, Integer userId);

    /**
     * 查询当前用户创建的小队信息
     * @param creatorId
     * @return
     */
    List<Team> getTeamsByCurrentUser(int creatorId);

    /**
     * 根据关键词查询志愿活动的队伍
     * @param keyword
     * @return
     */
    List<Team> getTeamsByKeyWord(String keyword);
}




