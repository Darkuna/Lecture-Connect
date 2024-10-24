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
    private boolean fixed;
    private int duration;
    private int semester;
    private String studyType;
    private List<TimingDTO> timingConstraints;
    private TimingDTO timing;
    private RoomTableDTO roomTable;
    private String courseId;

    @Override
    public String toString(){
        return String.format("%s: %s %s %s %s", name, studyType, roomTable.getRoomId(), timing, courseId);
    }
}

