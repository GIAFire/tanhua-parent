package com.itheima;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@MapperScan("com.itheima.mapper")
@EnableDubbo(scanBasePackages = "com.itheima.service.db")
public class DbServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DbServiceApplication.class, args);
        System.out.println("db启动成功");
    }
}
