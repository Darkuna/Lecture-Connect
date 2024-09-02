package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TimeTableCreationDTO {
    private String name;
    private String semester;
    private int year;
    private String status;

    private List<CourseDTO> courses = new ArrayList<>();
    private List<RoomDTO> rooms = new ArrayList<>();
}
