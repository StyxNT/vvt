package com.styxnt.vvtserver.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.styxnt.vvtserver.constants.MailConstants;
import com.styxnt.vvtserver.mapper.TeamMapper;
import com.styxnt.vvtserver.mapper.UserMapper;
import com.styxnt.vvtserver.pojo.MailLog;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.pojo.User;
import com.styxnt.vvtserver.service.MailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * 邮件发送定时任务
 *
 * @author StyxNT
 * @date 2021/7/31
 */
@Component
public class MailTask {

    @Autowired
    private MailLogService mailLogService;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeamMapper teamMapper;

    /**
     * 邮件发送的定时任务
     * 每十秒钟执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask() {
        List<MailLog> mailLogList = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0));

        mailLogList.forEach(mailLog -> {
            //重试次数超过三次就更新状态为投递失败
            if (MailConstants.MAX_TRY_COUNT < mailLog.getCount()) {
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status", MailConstants.FAILURE)
                        .eq("msgId", mailLog.getMsgId()));
            } else {
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .eq("msgId", mailLog.getMsgId())
                        .set("count", mailLog.getCount() + 1)
                        .set("updateTime", LocalDateTime.now())
                );
                HashMap<String,Object> teamMap=new HashMap<>();

                int userId= mailLog.getUserId();
                Team team=teamMapper.selectById(mailLog.getTeamId());

                User user=userMapper.selectById(userId);
                user.setPassword("");
                user.setUserface("");
                teamMap.put("teamInfo",team);
                teamMap.put("userInfo",user);
                //重新发送任务
                rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
                        MailConstants.MAIL_ROUTING_KEY_NAME,
                        teamMap,
                        new CorrelationData(mailLog.getMsgId()));
            }

        });
    }
}
