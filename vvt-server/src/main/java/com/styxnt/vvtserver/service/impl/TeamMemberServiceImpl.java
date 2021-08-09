package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.styxnt.vvtserver.mapper.TeamMemberMapper;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.pojo.TeamMember;
import com.styxnt.vvtserver.service.ActivityService;
import com.styxnt.vvtserver.service.TeamMemberService;
import com.styxnt.vvtserver.service.TeamService;
import com.styxnt.vvtserver.utils.CommonResponse;
import com.styxnt.vvtserver.utils.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author StyxNT
 */
@Service
public class TeamMemberServiceImpl extends ServiceImpl<TeamMemberMapper, TeamMember>
implements TeamMemberService{

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ActivityService activityService;

    /**
     * 加入小队
     * @param teamMember
     * @return
     */
    @Override
    public CommonResponse joinTeam(TeamMember teamMember) {

        //获取小队信息
        Team team = teamService.getById(teamMember.getTeamId());
        if(team==null){
            return CommonResponse.error("加入失败!小队不存在");
        }

        //获取目前参与到该小队的人数
        int memberCount = teamMemberService.count(new QueryWrapper<TeamMember>().eq("team_id", teamMember.getTeamId()));

        //获取小队所参与活动的最大人数
        Integer maxParticipant = activityService.getById(team.getActivityId()).getMaxParticipant();

        if(memberCount>=maxParticipant){
            return CommonResponse.error("小队人数已满,无法参与!");
        }
        teamMember.setMemberId(SecurityContextUtil.getContextUser().getId());
        if(teamMemberService.save(teamMember)){
            return CommonResponse.success("加入成功");
        }
        return CommonResponse.error("未知错误，加入失败");
    }
}




