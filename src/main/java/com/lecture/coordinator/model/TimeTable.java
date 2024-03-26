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
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "time_table_courses",
            joinColumns = @JoinColumn(name = "time_table_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "time_table_rooms",
            joinColumns = @JoinColumn(name = "time_table_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> rooms;

    @OneToMany(mappedBy = "timeTable", fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoomTable> roomTables;

    @OneToMany(mappedBy = "timeTable", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
