package com.styxnt.vvtserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class VvtServerApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    String uuid="156512165468";
    @Test
    void contextLoads() {

//        String uuid = UUID.randomUUID().toString();

        if (redisTemplate.opsForValue().setIfAbsent("UUIDTest:"+uuid,"OK",10, TimeUnit.MINUTES)) {
            System.out.println("success");
        }else {
            System.out.println("error");
        }

    }

    @Test
    void redisDeleteTest(){
        System.out.println(redisTemplate.delete("UUIDTest:" + uuid));
    }

}
