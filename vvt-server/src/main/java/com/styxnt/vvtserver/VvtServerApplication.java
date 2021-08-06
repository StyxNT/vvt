package com.styxnt.vvtserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.styxnt.vvtserver.mapper")
public class VvtServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VvtServerApplication.class, args);
    }

}
