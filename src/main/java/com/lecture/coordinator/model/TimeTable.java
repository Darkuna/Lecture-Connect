package com.lecture.coordinator.model;

import com.lecture.coordinator.model.enums.Semester;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TimeTable implements Persistable<Long>, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Semester semester;
    @Column(name = "academic_year")
    private int year;
    @OneToMany(mappedBy = "timeTable", fetch= FetchType.LAZY)
    private List<RoomTable> roomTables;
    @OneToMany(mappedBy = "timeTable", fetch = FetchType.LAZY)
    private List<CourseSession> courseSessions;

    //CONSTRUCTOR
    public TimeTable(){
        this.roomTables = new ArrayList<>();
        this.courseSessions = new ArrayList<>();
    }

    //GETTERS AND SETTERS
    @Override
    public Long getId() {
        return id;
    }


    //OTHER METHODS
    @Override
    public boolean isNew() {
        return id == null;
    }

    public void addRoomTable(RoomTable roomTable){
        if(roomTable != null){
            roomTables.add(roomTable);
        }
    }

    public void removeRoomTable(RoomTable roomTable){
        if(roomTable != null){
            roomTables.remove(roomTable);
        }
    }

    public void addCourseSessions(List<CourseSession> courseSessions){
        if(courseSessions != null){
            this.courseSessions.addAll(courseSessions);
        }
    }

    public void removeCourseSession(CourseSession courseSession){
        if(courseSessions != null){
            this.courseSessions.remove(courseSession);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTable timeTable = (TimeTable) o;
        return id != null && id.equals(timeTable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public List<CourseSession> getUnassignedCourseSessions(){
        List<CourseSession> unassignedCourseSessions = new ArrayList<>();
        for(CourseSession courseSession : courseSessions){
            if(!courseSession.isAssigned()){
                unassignedCourseSessions.add(courseSession);
            }
        }
        return unassignedCourseSessions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<RoomTable> getRoomTables() {
        return roomTables;
    }

    public void setRoomTables(List<RoomTable> roomTables) {
        this.roomTables = roomTables;
    }

    public List<CourseSession> getCourseSessions() {
        return courseSessions;
    }

    public void setCourseSessions(List<CourseSession> courseSessions) {
        this.courseSessions = courseSessions;
    }
}
