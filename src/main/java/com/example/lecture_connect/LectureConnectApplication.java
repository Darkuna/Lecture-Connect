package com.example.lecture_connect;

import com.example.lecture_connect.configs.SecurityConfig;
import com.example.lecture_connect.configs.SecurityFilter;
import com.example.lecture_connect.configs.SpringFoxConfig;
import com.sun.tools.javac.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LectureConnectApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(LectureConnectApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application
                .sources(Main.class, SecurityConfig.class, SecurityFilter.class, SpringFoxConfig.class);
    }
}
