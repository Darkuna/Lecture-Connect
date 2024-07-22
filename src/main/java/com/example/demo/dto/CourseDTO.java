package com.example.demo.dto;

import com.example.demo.models.enums.CourseType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseDTO {
    private String id;
    private String courseType;
    private String name;
    private String lecturer;
    private int semester;
    private int duration;
    private int numberOfParticipants;
    private boolean computersNecessary;
    private List<TimingDTO> timingConstraints = new ArrayList<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    private int numberOfGroups;
    private boolean isSplit;
    private List<Integer> splitTimes;
}
