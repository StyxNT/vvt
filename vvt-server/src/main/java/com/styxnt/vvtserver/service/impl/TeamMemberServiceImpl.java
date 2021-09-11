package com.styxnt.vvtserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.styxnt.vvtserver.constants.MailConstants;
import com.styxnt.vvtserver.mapper.MailLogMapper;
import com.styxnt.vvtserver.mapper.TeamMemberMapper;
import com.styxnt.vvtserver.mapper.UserMapper;
import com.styxnt.vvtserver.pojo.MailLog;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.pojo.TeamMember;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.ActivityService;
import com.styxnt.vvtserver.service.TeamMemberService;
import com.styxnt.vvtserver.service.TeamService;
import com.styxnt.vvtserver.utils.CommonResponse;
import com.styxnt.vvtserver.utils.SecurityContextUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private TeamMemberMapper teamMemberMapper;

    @Autowired
    private MailLogMapper mailLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 加入小队
     * @param teamMember
     * @return
     */
    @Override
    @Transactional
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

        int userId =SecurityContextUtil.getContextUser().getId();

        Integer count = teamMemberMapper.selectCount(new QueryWrapper<TeamMember>()
                .eq("team_id", teamMember.getTeamId())
                .eq("member_id", userId));

        if(count>0){
            return CommonResponse.error("你已加入该小队，无法重复加入");
        }

        if(memberCount>=maxParticipant){
            return CommonResponse.error("小队人数已满,无法参与!");
        }
        teamMember.setMemberId(userId);
        if(teamMemberService.save(teamMember)){
            //注册成功就通过消息队列发送欢迎邮件
            MailLog mailLog=new MailLog();
            //向数据库中存入发送的消息
            String msgId = UUID.randomUUID().toString();
            mailLog.setMsgId(msgId);
            mailLog.setTeamId(team.getId());
            mailLog.setUserId(userId);
            mailLog.setStatus(0);
            mailLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailLog.setCount(0);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            mailLogMapper.insert(mailLog);

            team.setActivity(activityService.getById(team.getActivityId()));
            team.setTeacher(userMapper.selectById(team.getTeacherId()));

            HashMap<String,Object> teamMap=new HashMap<>();

            User user=userMapper.selectById(userId);
            user.setPassword("");
            user.setUserface("");
            teamMap.put("teamInfo",team);
            teamMap.put("userInfo",user);

            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME,
                    teamMap,
                    new CorrelationData(msgId));

            return CommonResponse.success("加入成功");
        }
        return CommonResponse.error("未知错误，加入失败");
    }

    /**
     * 查询指定小队的成员
     * @param teamId
     * @return
     */
    @Override
    public List<TeamMember> getTeamMembers(Integer teamId) {
        return teamMemberMapper.getTeamMembers(teamId);
    }
}




