package com.qijiejin.studentinfo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.qijiejin.studentinfo.mapper")
public class StudentInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentInfoApplication.class, args);
    }
}
