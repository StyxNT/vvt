package com.styxnt.vvtserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.styxnt.vvtserver.pojo.TeamMember;
import com.styxnt.vvtserver.utils.CommonResponse;

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
}
