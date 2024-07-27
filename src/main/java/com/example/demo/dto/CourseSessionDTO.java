package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseSessionDTO {
    private Long id;
    private String name;
    private boolean isAssigned;
    private boolean isFixed;
    private int duration;
    private int semester;
    private String studyType;
    private List<TimingDTO> timingConstraints;
    private TimingDTO timing;
    private RoomTableDTO roomTable;
}