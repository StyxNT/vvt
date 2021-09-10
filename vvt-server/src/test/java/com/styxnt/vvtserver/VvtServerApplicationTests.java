package com.styxnt.vvtserver;

import com.styxnt.vvtserver.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class VvtServerApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        Object test = redisTemplate.opsForValue().get("test");
        System.out.println((User)test);
    }

}
