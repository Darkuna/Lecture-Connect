package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class TimingDTO {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String day;
    private String timingType;
}
