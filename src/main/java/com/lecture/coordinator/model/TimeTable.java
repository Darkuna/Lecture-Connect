package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class TimeTable implements Persistable<Long>, Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Semester semester;
    @Column(name = "academic_year")
    private int year;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Room> rooms;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "timeTable", fetch= FetchType.LAZY)
    private List<RoomTable> roomTables;

    @Transient
    private List<CourseSession> courseSessions;

    //CONSTRUCTOR
    public TimeTable(){}

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

    //OTHER METHODS
    @Override
    public boolean isNew() {
        return id == null;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
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
