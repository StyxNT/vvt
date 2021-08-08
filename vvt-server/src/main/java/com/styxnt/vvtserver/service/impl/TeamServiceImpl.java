package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.styxnt.vvtserver.mapper.TeamMapper;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.pojo.TeamMember;
import com.styxnt.vvtserver.service.TeamMemberService;
import com.styxnt.vvtserver.service.TeamService;
import com.styxnt.vvtserver.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author StyxNT
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamService teamService;

    /**
     * 删除小队
     *删除小队中的成员(team_member)表，再删除小队(team表)
     * @param teamId
     * @return
     */
    @Override
    @Transactional
    public CommonResponse deleteTeam(Integer teamId) {

        teamMemberService.remove(new QueryWrapper<TeamMember>().eq("team_id",teamId));
        if(teamService.removeById(teamId)){
            return CommonResponse.success("删除成功");
        }
        return CommonResponse.error("删除失败");
    }
}




