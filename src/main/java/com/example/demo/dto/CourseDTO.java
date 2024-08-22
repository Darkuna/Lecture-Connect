package com.example.demo.dto;

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
    private String studyType;
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
    private boolean split;
    private List<Integer> splitTimes;

    @Override
    public String toString() {
        return "CourseDTO{" +
                "id='" + id + '\'' +
                ", courseType='" + courseType + '\'' +
                ", studyType='" + studyType + '\'' +
                ", name='" + name + '\'' +
                ", lecturer='" + lecturer + '\'' +
                ", semester=" + semester +
                ", duration=" + duration +
                ", numberOfParticipants=" + numberOfParticipants +
                ", computersNecessary=" + computersNecessary +
                ", timingConstraints=" + timingConstraints +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", numberOfGroups=" + numberOfGroups +
                ", isSplit=" + split +
                ", splitTimes=" + splitTimes +
                '}';
    }
}
