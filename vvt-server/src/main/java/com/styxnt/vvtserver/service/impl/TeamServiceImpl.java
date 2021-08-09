package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.styxnt.vvtserver.mapper.TeamMapper;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.pojo.TeamMember;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.TeamMemberService;
import com.styxnt.vvtserver.service.TeamService;
import com.styxnt.vvtserver.utils.CommonResponse;
import com.styxnt.vvtserver.utils.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author StyxNT
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamMapper teamMapper;

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

    /**
     * 查询当前用户创建的小队信息
     * @return
     */
    @Override
    public List<Team> getTeamsByCurrentUser() {
        int creatorId= ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        return teamMapper.getAllTeams(creatorId,null,null);
    }

    /**
     * 根据关键词查询志愿活动的队伍
     * @param keyword
     * @return
     */
    @Override
    public List<Team> getTeamsByKeyWord(String keyword) {
        return teamMapper.getAllTeams(null,keyword,null);
    }

    /**
     * 查询当前用户参与的所有活动
     * @return
     */
    @Override
    public List<Team> getMyTeams() {
        Integer userId= SecurityContextUtil.getContextUser().getId();
        return teamMapper.getAllTeams(null, null, userId);
    }
}




