package com.lecture.coordinator.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class RoomTable implements Persistable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;
    @ManyToOne
    private TimeTable timeTable;
    @OneToMany(mappedBy="roomTable", fetch = FetchType.LAZY)
    private List<CourseSession> assignedCourseSessions;
    @Transient
    private AvailabilityMatrix availabilityMatrix;

    public AvailabilityMatrix getAvailabilityMatrix() {
        return availabilityMatrix;
    }
    public void setAvailabilityMatrix(AvailabilityMatrix availabilityMatrix) {
        this.availabilityMatrix = availabilityMatrix;
    }
    public List<CourseSession> getAssignedCourseSessions() {
        return assignedCourseSessions;
    }

    public void setAssignedCourseSessions(List<CourseSession> assignedCourseSessions) {
        this.assignedCourseSessions = assignedCourseSessions;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomTable roomTable = (RoomTable) o;
        return id != null && id.equals(roomTable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
