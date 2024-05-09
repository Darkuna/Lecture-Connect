package com.example.demo.dto;

import com.example.demo.models.enums.CourseType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseDTO {
    private String id;
    private String name;
    private CourseType courseType;
    private String lecturer;
    private int semester;
    private int duration;
    private int numberOfParticipants;
    private boolean computersNecessary;
    private List<TimingDTO> timingConstraints = new ArrayList<>();
}
