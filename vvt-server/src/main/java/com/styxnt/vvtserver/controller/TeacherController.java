package com.styxnt.vvtserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.styxnt.vvtserver.pojo.Activity;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.pojo.TeamMember;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.ActivityService;
import com.styxnt.vvtserver.service.TeamMemberService;
import com.styxnt.vvtserver.service.TeamService;
import com.styxnt.vvtserver.utils.CommonResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author StyxNT
 * @date 2021/8/7
 */
@RestController
@RequestMapping("/teacher")
@PreAuthorize("hasRole('teacher')")
public class TeacherController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamMemberService teamMemberService;

    @ApiOperation(value = "添加活动")
    @PostMapping("/activity")
    public CommonResponse addActivity(@RequestBody Activity activity) {
        activity.setCreateDate(LocalDate.now());
        if (activityService.save(activity)) {
            return CommonResponse.success("添加活动成功！");
        }
        return CommonResponse.error("添加活动失败");
    }

    @ApiOperation(value = "更新活动信息")
    @PutMapping("/activity")
    public CommonResponse updateActivity(@RequestBody Activity activity) {
        if (activityService.updateById(activity)) {
            return CommonResponse.success("更新活动成功!");
        }
        return CommonResponse.error("更新活动失败!");
    }

    @ApiOperation(value = "删除活动")
    @DeleteMapping("/activity/{activityId}")
    public CommonResponse deleteActivity(@PathVariable Integer activityId) {
        if (activityService.removeById(activityId)) {
            return CommonResponse.success("删除成功！");
        }
        return CommonResponse.error("删除失败！");
    }

    @ApiOperation(value = "查询当前用户创建的活动")
    @GetMapping("/activity")
    public List<Activity> getActivities() {
        int userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return activityService.list(new QueryWrapper<Activity>().eq("creator_id", userId));
    }

    @ApiOperation(value = "创建小队")
    @PostMapping("/team")
    public CommonResponse createTeam(@RequestBody Team team) {
        team.setCreateTime(LocalDate.now());
        if (teamService.save(team)) {
            return CommonResponse.success("添加成功！");
        }
        return CommonResponse.error("添加失败");
    }

    @ApiOperation(value = "删除小队")
    @DeleteMapping("/team/{teamId}")
    public CommonResponse deleteTeam(@PathVariable Integer teamId) {
        return teamService.deleteTeam(teamId);
    }

    @ApiOperation(value = "删除小队中的某个成员")
    @DeleteMapping("/team/{teamId}/member/{memberId}")
    public CommonResponse deleteTeamMember(@PathVariable Integer teamId, @PathVariable Integer memberId) {

        if (teamMemberService.remove(new QueryWrapper<TeamMember>().eq("team_id", teamId).eq("member_id", memberId))) {
            return CommonResponse.success("删除成功!");
        }
        return CommonResponse.error("删除失败!");
    }

    @ApiOperation(value = "向小队中添加成员")
    @PostMapping("/team/member/")
    public CommonResponse addTeamMember(@RequestBody TeamMember teamMember) {
        if (teamMemberService.save(teamMember)) {
            return CommonResponse.success("添加成功！");
        }
        return CommonResponse.error("添加失败");
    }

    @ApiOperation(value = "为小队成员添加评论")
    @PutMapping("/team/member/comment")
    public CommonResponse addComment(@RequestBody TeamMember comment) {
        if (teamMemberService.update(comment,
                new QueryWrapper<TeamMember>()
                        .eq("team_id", comment.getTeamId())
                        .eq("member_id", comment.getMemberId()))) {
            return CommonResponse.success("评价成功!");

        }
        return CommonResponse.error("评价失败!");
    }

    @ApiOperation(value = "修改小队信息")
    @PutMapping("/team")
    public CommonResponse updateTeam(@RequestBody Team team) {
        if (teamService.updateById(team)) {
            return CommonResponse.success("更新成功!");
        }
        return CommonResponse.error("更新失败!");
    }

    @ApiOperation(value = "查询当前用户创建的小队信息")
    @GetMapping("/team/")
    public List<Team> getTeamsByCurrentUser(){
        return teamService.getTeamsByCurrentUser();
    }

}
