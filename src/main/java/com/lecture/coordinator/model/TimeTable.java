package com.lecture.coordinator.model;

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
    @OneToMany(mappedBy = "timeTable", fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoomTable> roomTables;
    @OneToMany(mappedBy = "timeTable", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
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

    public void addCourseSessions(List<CourseSession> courseSessions){
        if(courseSessions != null){
            this.courseSessions.addAll(courseSessions);
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
}
