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
        //TODO: create test for a course without split or groups
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfSplitCourse(){
        //TODO: create test for a split course
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfCourseWithGroups(){
        //TODO: create test for a group course
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateCourseSessionOfCourseWithFixedTiming(){
        //TODO: create test for a course with fixed timing
    }
}
