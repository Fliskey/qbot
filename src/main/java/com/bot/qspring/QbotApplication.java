package com.bot.qspring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.baomidou.mybatisplus.samples.quickstart.mapper")
@MapperScan("com.bot.qspring.dao")
@MapperScan("com.bot.qspring.mapper")
@EnableScheduling
public class QbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(QbotApplication.class, args);
    }

}
