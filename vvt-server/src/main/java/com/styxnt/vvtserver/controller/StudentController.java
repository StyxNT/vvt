package com.styxnt.vvtserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.pojo.TeamMember;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.TeamMemberService;
import com.styxnt.vvtserver.service.TeamService;
import com.styxnt.vvtserver.service.UserService;
import com.styxnt.vvtserver.utils.CommonResponse;
import com.styxnt.vvtserver.utils.SecurityContextUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生用户访问接口
 * @author StyxNT
 * @date 2021/8/8
 */
@RestController
@RequestMapping("/student")
@PreAuthorize("hasRole('student')")
public class StudentController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询所有活动队伍信息")
    @GetMapping("/team/")
    public List<Team> getTeams(@RequestParam(required = false) String keyword){
        return teamService.getTeamsByKeyWord(keyword);
    }

    @ApiOperation(value = "加入指定队伍")
    @PostMapping("/team")
    public CommonResponse  joinTeam(@RequestBody TeamMember teamMember){
        return teamMemberService.joinTeam(teamMember);
    }

    @ApiOperation(value = "退出队伍")
    @DeleteMapping("/team/{teamId}")
    public CommonResponse quitTeam(@PathVariable Integer teamId){
        int userId= SecurityContextUtil.getContextUser().getId();
        if(teamMemberService.remove(new QueryWrapper<TeamMember>().eq("team_id",teamId).eq("member_id",userId))){
            return CommonResponse.success("退出成功");
        }
        return CommonResponse.error("退出失败");
    }

    @ApiOperation(value = "查询当前用户参与的所有活动")
    @GetMapping("/myteam")
    public List<Team> getMyTeams(){
        return teamService.getMyTeams();
    }

    @ApiOperation(value = "查询单个活动的成绩")
    @GetMapping("/team/{teamId}/comment")
    public TeamMember getTeamComment(@PathVariable Integer teamId){
        int userId=SecurityContextUtil.getContextUser().getId();
        return teamMemberService.getOne(new QueryWrapper<TeamMember>().eq("team_id",teamId).eq("member_id",userId));
    }

    @ApiOperation(value = "根据小队id查询老师信息")
    @GetMapping("/team/teacher/{teamId}")
    public User getTeacherInfoByTeamId(@PathVariable Integer teamId){
        return userService.getTeacherInfoByTeamId(teamId);
    }

}
