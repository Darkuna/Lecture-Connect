package com.lecture.coordinator.tests.service;

import com.lecture.coordinator.services.TimingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
public class TimingServiceTest {
    @Autowired
    private TimingService timingService;

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testCreateTiming(){
        //TODO: create a test for creating a timing
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testUpdateTiming(){
        //TODO: create a test for updating a given timing
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfRoom(){
        //TODO: create a test for loading all timing constraints of a given room
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER"})
    public void testLoadTimingConstraintsOfCourse(){
        //TODO: create a test for loading all timing constraints of a given course
    }
}
