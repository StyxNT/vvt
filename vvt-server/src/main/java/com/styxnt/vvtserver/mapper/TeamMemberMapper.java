package com.styxnt.vvtserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.styxnt.vvtserver.pojo.TeamMember;

import java.util.List;

/**
 * @Entity com.styxnt.vvtserver.pojo.TeamMember
 */
public interface TeamMemberMapper extends BaseMapper<TeamMember> {

    /**
     * 查询指定小队的成员
     * @param teamId
     * @return
     */
    List<TeamMember> getTeamMembers(Integer teamId);



}




