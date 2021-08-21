package com.styxnt.vvtserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.styxnt.vvtserver.pojo.TeamMember;
import com.styxnt.vvtserver.utils.CommonResponse;

import java.util.List;

/**
 *
 */
public interface TeamMemberService extends IService<TeamMember> {

    /**
     * 加入小队
     * @param teamMember
     * @return
     */
    CommonResponse joinTeam(TeamMember teamMember);

    /**
     * 查询指定小队的成员
     * @param teamId
     * @return
     */
    List<TeamMember> getTeamMembers(Integer teamId);
}
