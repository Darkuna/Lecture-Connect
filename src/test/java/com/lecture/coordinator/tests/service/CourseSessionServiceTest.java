package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.services.CourseSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class CourseSessionServiceTest {
    @Autowired
    private CourseSessionService courseSessionService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfNormalCourse(){

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfSplitCourse(){

    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfCourseWithGroups(){

    }
}
