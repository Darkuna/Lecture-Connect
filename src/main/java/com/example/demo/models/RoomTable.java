package com.example.demo.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;

@Setter
@Entity
@EqualsAndHashCode
public class RoomTable implements Persistable<Long>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy="roomTable", fetch = FetchType.LAZY)
    private List<CourseSession> assignedCourseSessions;
    @OneToMany(mappedBy="roomTable", fetch = FetchType.LAZY)
    private List<Timing> timingConstraints;
    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;
    @ManyToOne
    private TimeTable timeTable;

    @Transient
    private AvailabilityMatrix availabilityMatrix;

    public AvailabilityMatrix getAvailabilityMatrix() {
        return availabilityMatrix;
    }

    public List<CourseSession> getAssignedCourseSessions() {
        return assignedCourseSessions;
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

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public List<Timing> getTimingConstraints() {
        return timingConstraints;
    }

}
