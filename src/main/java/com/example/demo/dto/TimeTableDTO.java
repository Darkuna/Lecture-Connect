package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TimeTableDTO {
    private Long id;
    private String semester;
    private int year;
    private List<Long> roomTableIds;
    private List<Long> courseSessionIds;
}