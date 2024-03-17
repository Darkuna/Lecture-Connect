package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class RoomTable implements Persistable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Room room;
    @ManyToOne
    private TimeTable timeTable;
    @Transient
    private Set<CourseSession> assignedCourses;

    public Set<CourseSession> getAssignedCourses() {
        return assignedCourses;
    }

    public void setAssignedCourses(Set<CourseSession> assignedCourses) {
        this.assignedCourses = assignedCourses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }
}
