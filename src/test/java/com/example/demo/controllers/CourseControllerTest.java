package com.example.demo.controllers;

import at.uibk.leco.controllers.CourseController;
import at.uibk.leco.dto.CourseDTO;
import at.uibk.leco.models.Course;
import at.uibk.leco.services.CourseService;
import at.uibk.leco.services.DTOConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CourseService courseService;
    @MockBean
    private DTOConverter dtoConverter;
    @Test
    public void testGetCourses() throws Exception {
        List<Course> courses = List.of(new Course(), new Course(), new Course());
        when(courseService.loadAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/api/courses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testCreateCourse() throws Exception {
        CourseDTO courseDto = new CourseDTO();
        courseDto.setName("New Course");
        courseDto.setSemester(1);

        Course course = new Course();
        course.setName("New Course");
        course.setSemester(1);

        when(courseService.createCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCourse() throws Exception {
        String courseId = "1";
        CourseDTO courseDto = new CourseDTO();
        courseDto.setName("Updated Course");
        courseDto.setSemester(1);

        Course updatedCourse = new Course();
        updatedCourse.setId(courseId);
        updatedCourse.setName("Updated Course");
        updatedCourse.setSemester(1);

        when(courseService.loadCourseById(courseId)).thenReturn(updatedCourse);
        when(courseService.updateCourse(any(Course.class))).thenReturn(updatedCourse);

        mockMvc.perform(put("/api/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(courseDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCourse() throws Exception {
        String courseId = "1";

        doNothing().when(courseService).deleteCourse(any(Course.class));

        mockMvc.perform(delete("/api/courses/{id}", courseId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMultipleCourses() throws Exception {
        List<String> courseIds = List.of("1", "2", "3");

        doNothing().when(courseService).deleteMultipleCourses(courseIds);

        mockMvc.perform(delete("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(courseIds)))
                .andDo(print())
                .andExpect(status().isOk());
    }

}

