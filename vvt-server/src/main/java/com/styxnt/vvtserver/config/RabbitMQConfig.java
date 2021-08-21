package com.styxnt.vvtserver.config;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.styxnt.vvtserver.pojo.MailConstants;
import com.styxnt.vvtserver.pojo.MailLog;
import com.styxnt.vvtserver.service.MailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author StyxNT
 * @date 2021/7/31
 */
@Configuration
public class RabbitMQConfig {

    public static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        /*
          设置确认回调,确认消息是否到达broker
          data:消息的唯一标识
          ack:确认结果
          cause:失败原因
         */
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = data.getId();
            if (ack) {
                LOGGER.info("{}=========>消息发送成功", msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 1).eq("msgId", msgId));
            } else {
                LOGGER.error("{}=========>消息发送失败", msgId);
            }
        });

        /*
          消息失败回调，例如router不到queue
          returnedMessage可以返回以下信息
          message:消息主题
          replyCode:响应码
          replyText:响应描述
          exchange:交换机
          routingKey:路由键
         */
        rabbitTemplate.setReturnsCallback((returnedMessage) -> {
            LOGGER.error("{}=========>消息发送失败", returnedMessage.getMessage().getBody());
        });

        return rabbitTemplate;
    }


    @Bean
    public Queue queue() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }


}
