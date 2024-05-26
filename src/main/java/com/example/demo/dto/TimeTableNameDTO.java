package com.example.demo.dto;

import com.example.demo.models.enums.Semester;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

/**
 * This DTO is used for timetable selection in home.component.ts
 */
@Getter
@Setter
public class TimeTableNameDTO {
    private Long id;
    private Semester semester;
    private int year;

    public TimeTableNameDTO(Long id, Semester semester, int year) {
        this.id = id;
        this.semester = semester;
        this.year = year;
    }
}
