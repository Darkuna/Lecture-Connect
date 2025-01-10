package at.uibk.leco.dto;

import at.uibk.leco.models.enums.Semester;
import lombok.Getter;
import lombok.Setter;

/**
 * This DTO is used for timetable selection in home.component.ts
 */
@Getter
@Setter
public class TimeTableNameDTO {
    private String name;
    private long id;
    private Semester semester;
    private int year;

    public TimeTableNameDTO(long id, Semester semester, int year, String name) {
        this.name = name;
        this.id = id;
        this.semester = semester;
        this.year = year;
    }
}



