package com.example.lecture_connect;

import com.example.lecture_connect.models.Userx;
import com.example.lecture_connect.repository.UserxRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Stream;

@SpringBootApplication
public class LectureConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LectureConnectApplication.class, args);
    }
}
