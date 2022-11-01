package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MongoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoServiceApplication.class,args);
        System.out.println("Mongo启动成功");
    }
}
