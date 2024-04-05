package com.example.lecture_connect;

import com.example.lecture_connect.configs.SecurityConfig;
import com.example.lecture_connect.configs.SecurityFilter;
import com.example.lecture_connect.configs.SpringFoxConfig;
import com.sun.tools.javac.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class LectureConnectApplication {
    public static void main(String[] args) {
        SpringApplication.run(LectureConnectApplication.class, args);
    }


}
