package com.styxnt.vvtmail;

import com.rabbitmq.client.Channel;
import com.styxnt.vvtserver.constants.MailConstants;
import com.styxnt.vvtserver.pojo.Team;
import com.styxnt.vvtserver.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 消息接受者
 *
 * @author StyxNT
 * @date 2021/7/30
 */
@Component
public class MailReceiver {

    public static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {

        HashMap<String,Object> teamMap = (HashMap) message.getPayload();
        User user=(User) teamMap.get("userInfo");
        Team team=(Team) teamMap.get("teamInfo");

        MessageHeaders headers = message.getHeaders();

        //消息序号
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //消息的UUID
        String msgId = (String) headers.get("spring_returned_message_correlation");

        //判断是否已存在消息id（setnx）如果返回false则说明已经被消费过，返回true表示未被消费，将消息的id存入redis
        if(redisTemplate.opsForValue().setIfAbsent(MailConstants.MAIL_REDIS_PREFIX+msgId,"OK",1, TimeUnit.DAYS)){
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            //尝试生成并发送消息
            try {
                //发件人
                helper.setFrom(mailProperties.getUsername());
                //收件人
                helper.setTo(user.getEmail());
                //主题
                helper.setSubject("志愿活动小队欢迎邮件");
                //发送日期
                helper.setSentDate(new Date());
                //邮件内容
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append("恭喜你成功加入");
                stringBuilder.append(team.getActivity().getName());
                stringBuilder.append("小队!\n");
                stringBuilder.append("你的指导老师为:");
                stringBuilder.append(team.getTeacher().getName());
                stringBuilder.append("老师的练习方式为:\n\t\t");
                stringBuilder.append("手机号:").append(team.getTeacher().getPhone());
                stringBuilder.append("邮箱:").append(team.getTeacher().getEmail());

                String mail=stringBuilder.toString();
                helper.setText(mail);
                javaMailSender.send(mimeMessage);
                LOGGER.info("邮件发送成功");
                //手动确认消息
                channel.basicAck(tag, false);
            } catch (Exception e) {
                //消费失败删除Redis中的消息id
                redisTemplate.delete(MailConstants.MAIL_REDIS_PREFIX+msgId);
                /*
                 * requeue:是否退回到队列
                 */
                try {
                    channel.basicNack(tag, false, true);
                } catch (IOException ex) {
                    LOGGER.error("邮件发送失败==========>{}", e.getMessage());
                }
                LOGGER.error("邮件发送失败==========>{}", e.getMessage());
            }
        }else{

            LOGGER.error("消息已被消费=======>{}", msgId);
            /*
             *手动确认消息
             * tag:消息序号
             * multiple:是否确认多条
             */
            try {
                channel.basicAck(tag, false);
            } catch (IOException e) {
                LOGGER.error("消息确认失败==========>{}", e.getMessage());
            }
        }


    }
}
